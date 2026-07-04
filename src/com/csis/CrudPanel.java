package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class CrudPanel extends JPanel {
    private final CrudConfig cfg;
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    private final JTextField id = new JTextField(8);
    private final JTextField search = new JTextField(18);
    private final Map<String, JTextField> fields = new LinkedHashMap<>();

    private final String insertSql;
    private final String updateSql;
    private final String deleteSql;
    private final String searchSql;

    public CrudPanel(CrudConfig c, UserSession s) {
        super(new BorderLayout(8, 8));
        this.cfg = c;
        this.session = s;

        this.insertSql = "INSERT INTO " + cfg.table + "(" + Sql.csvList(cfg.fields) + ") VALUES("
                + Sql.placeholders(cfg.fields.length) + ")";
        this.updateSql = "UPDATE " + cfg.table + " SET " + Sql.assignmentList(cfg.fields)
                + " WHERE " + cfg.idColumn + " = ?";
        this.deleteSql = "DELETE FROM " + cfg.table + " WHERE " + cfg.idColumn + " = ?";

        this.searchSql = "SELECT * FROM (" + cfg.selectSql + ") AS crud_base WHERE CAST("
            + cfg.searchColumn + " AS CHAR) LIKE ?";

        buildUI();
        refresh();
    }

    private void buildUI() {
        // Top Header and Search Bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title(cfg.title), BorderLayout.WEST);

        JPanel queryPanel = new JPanel();
        queryPanel.add(new JLabel("Search:"));
        queryPanel.add(search);

        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnExport = new JButton("Export CSV");
        queryPanel.add(btnSearch);
        queryPanel.add(btnRefresh);
        queryPanel.add(btnExport);
        headerPanel.add(queryPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Right-Side Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Record Form"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        id.setEditable(false);
        
        // --- UPGRADE: Formatted ID Label (Removed Bold) ---
        JLabel lblId = new JLabel(formatLabelName(cfg.idColumn));
        lblId.setFont(new Font("Tahoma", Font.PLAIN, 12)); 
        g.gridx = 0; g.gridy = row; formPanel.add(lblId, g);
        g.gridx = 1; formPanel.add(id, g);
        row++;

        for (String fieldName : cfg.fields) {
            JTextField tf = new JTextField(17);
            fields.put(fieldName, tf);
            
            // --- UPGRADE: Formatted Field Labels (Removed Bold) ---
            JLabel lblField = new JLabel(formatLabelName(fieldName));
            lblField.setFont(new Font("Tahoma", Font.PLAIN, 12)); 
            
            g.gridx = 0; g.gridy = row; formPanel.add(lblField, g);
            g.gridx = 1; formPanel.add(tf, g);
            row++;
        }

        // Form Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JButton btnNew = new JButton("New");
        JButton btnAdd = new JButton("Add");
        JButton btnUpd = new JButton("Update");
        JButton btnDel = new JButton("Delete");
        buttonPanel.add(btnNew); buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpd); buttonPanel.add(btnDel);

        g.gridx = 0; g.gridy = row; g.gridwidth = 2;
        formPanel.add(buttonPanel, g);
        add(new JScrollPane(formPanel), BorderLayout.EAST);

        // Event Listeners
        btnRefresh.addActionListener(e -> refresh());
        btnSearch.addActionListener(e -> doSearch());
        search.addActionListener(e -> doSearch());
        btnExport.addActionListener(e -> CsvExporter.exportTable(this, table, cfg.title.replace(' ', '_') + ".csv"));
        btnNew.addActionListener(e -> clearForm());
        btnAdd.addActionListener(e -> insertRecord());
        btnUpd.addActionListener(e -> updateRecord());
        btnDel.addActionListener(e -> deleteRecord());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectRecord();
        });
    }

    private void refresh() {
        try {
            model.load(cfg.selectSql);
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void doSearch() {
        String term = search.getText().trim();
        if (term.isEmpty()) {
            refresh();
            return;
        }
        try {
            model.load(searchSql, "%" + term + "%");
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void clearForm() {
        id.setText("");
        for (JTextField f : fields.values()) f.setText("");
        table.clearSelection();
    }

    private Object getFieldValue(String fieldName) {
        String val = fields.get(fieldName).getText().trim();
        return val.isEmpty() ? null : val;
    }

    private boolean hasAnyValue() {
        for (JTextField f : fields.values()) {
            if (!f.getText().trim().isEmpty()) return true;
        }
        return false;
    }

    private void selectRecord() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        r = table.convertRowIndexToModel(r);

        int p = model.findColumnByName(cfg.idColumn);
        if (p >= 0) id.setText(String.valueOf(model.getValueAt(r, p)));

        for (String f : cfg.fields) {
            p = model.findColumnByName(f);
            if (p >= 0) {
                Object val = model.getValueAt(r, p);
                fields.get(f).setText(val == null ? "" : String.valueOf(val));
            }
        }
    }

    private void insertRecord() {
        if (!hasAnyValue()) {
            UIUtils.error(this, new IllegalArgumentException("Please fill in at least one field before adding a record."));
            return;
        }
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(insertSql)) {
            for (int i = 0; i < cfg.fields.length; i++) {
                p.setObject(i + 1, getFieldValue(cfg.fields[i]));
            }
            p.executeUpdate();
            Audit.log(session, "ADD_" + cfg.table, "New record");
            refresh();
            clearForm();
            UIUtils.info(this, "Record added successfully.");
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
    // The updateRecord method validates the form input, executes the UPDATE SQL statement, 
    // and handles success or error scenarios with appropriate user feedback.
    private void updateRecord() {
        if (id.getText().isEmpty()) {
            UIUtils.info(this, "Select a record from the table first.");
            return;
        }
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(updateSql)) {
            int i;
            for (i = 0; i < cfg.fields.length; i++) {
                p.setObject(i + 1, getFieldValue(cfg.fields[i]));
            }
            p.setObject(i + 1, id.getText().trim());

            int updated = p.executeUpdate();
            if (updated == 0) {
                UIUtils.error(this, new IllegalStateException("No record was updated. It may have been deleted by another user."));
                return;
            }
            Audit.log(session, "UPDATE_" + cfg.table, cfg.idColumn + "=" + id.getText());
            refresh();
            UIUtils.info(this, "Record updated successfully.");
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void deleteRecord() {
        if (id.getText().isEmpty() || !UIUtils.confirm(this, "Permanently delete selected record?")) return;
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(deleteSql)) {
            p.setString(1, id.getText().trim());
            int deleted = p.executeUpdate();
            if (deleted == 0) {
                UIUtils.error(this, new IllegalStateException("No record was deleted. It may already be gone."));
                return;
            }
            Audit.log(session, "DELETE_" + cfg.table, cfg.idColumn + "=" + id.getText());
            refresh();
            clearForm();
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    // Title Case Formatter Helper Method
    private String formatLabelName(String dbColumnName) {
        if (dbColumnName == null || dbColumnName.isEmpty()) return "";
        String[] words = dbColumnName.split("_");
        StringBuilder cleanName = new StringBuilder();
        
        for (String word : words) {
            if (word.equalsIgnoreCase("id")) {
                cleanName.append("ID");
            } else if (word.length() > 0) {
                cleanName.append(Character.toUpperCase(word.charAt(0)))
                         .append(word.substring(1).toLowerCase());
            }
            cleanName.append(" ");
        }
        return cleanName.toString().trim();
    }
}