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
    
    // THE FIX: Changed from JTextField to JComponent to support JComboBoxes
    private final Map<String, JComponent> fields = new LinkedHashMap<>();

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
        
        JLabel lblId = new JLabel(formatLabelName(cfg.idColumn));
        lblId.setFont(new Font("Tahoma", Font.PLAIN, 12)); 
        g.gridx = 0; g.gridy = row; formPanel.add(lblId, g);
        g.gridx = 1; formPanel.add(id, g);
        row++;

        for (String fieldName : cfg.fields) {
            JComponent comp;
            
            // THE FIX: Smart detection for ID fields to inject a Dropdown instead of a text box
            if (fieldName.equals("supplier_id") || fieldName.equals("ingredient_id") || 
                fieldName.equals("category_id") || fieldName.equals("batch_id") || 
                fieldName.equals("product_id") || fieldName.equals("customer_id") ||
                fieldName.equals("recorded_by") || fieldName.equals("category")) {
                
                JComboBox<String> combo = new JComboBox<>();
                loadSmartDropdown(combo, fieldName);
                comp = combo;
            } else {
                comp = new JTextField(17);
            }
            
            fields.put(fieldName, comp);
            
            JLabel lblField = new JLabel(formatLabelName(fieldName));
            lblField.setFont(new Font("Tahoma", Font.PLAIN, 12)); 
            
            g.gridx = 0; g.gridy = row; formPanel.add(lblField, g);
            g.gridx = 1; formPanel.add(comp, g);
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
    
    // --- NEW LOGIC: Dynamically populates the dropdown based on the field name ---
    private void loadSmartDropdown(JComboBox<String> cb, String fieldName) {
        cb.addItem(""); // Default empty option
        String sql = "";

        if (fieldName.equals("supplier_id")) {
            sql = "SELECT supplier_id, supplier_name FROM suppliers WHERE status = 'ACTIVE'";
        } else if (fieldName.equals("ingredient_id")) {
            sql = "SELECT ingredient_id, ingredient_name FROM ingredients";
        } else if (fieldName.equals("category_id")) {
            sql = "SELECT category_id, category_name FROM categories";
        } else if (fieldName.equals("batch_id")) {
            sql = "SELECT batch_id, batch_number FROM ingredient_batches";
        } else if (fieldName.equals("product_id")) {
            sql = "SELECT product_id, product_name FROM products";
        } else if (fieldName.equals("customer_id")) {
            sql = "SELECT cp.customer_id, u.full_name FROM customer_profiles cp JOIN users u ON cp.user_id = u.user_id";
        } else if (fieldName.equals("recorded_by")) {
            sql = "SELECT user_id, full_name FROM users WHERE role IN ('ADMIN', 'STAFF')";
        } else if (fieldName.equals("category")) {
            cb.addItem("UTILITIES");
            cb.addItem("MAINTENANCE");
            cb.addItem("INVENTORY SPOILAGE");
            cb.addItem("MARKETING");
            cb.addItem("MISCELLANEOUS");
            return; // Exit early since we don't need to run a DB query for this
        }

        if (!sql.isEmpty()) {
            try (Connection c = DB.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
                while (rs.next()) {
                    cb.addItem(rs.getInt(1) + " - " + rs.getString(2)); // ID - Name Format
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void refresh() {
        try { model.load(cfg.selectSql); } 
        catch (Exception e) { UIUtils.error(this, e); }
    }

    private void doSearch() {
        String term = search.getText().trim();
        if (term.isEmpty()) { refresh(); return; }
        try { model.load(searchSql, "%" + term + "%"); } 
        catch (Exception e) { UIUtils.error(this, e); }
    }

    // THE FIX: Clears both text fields and dropdowns
    private void clearForm() {
        id.setText("");
        for (JComponent comp : fields.values()) {
            if (comp instanceof JTextField) ((JTextField) comp).setText("");
            else if (comp instanceof JComboBox<?>) ((JComboBox<?>) comp).setSelectedIndex(0);
        }
        table.clearSelection();
    }

    // THE FIX: Intelligently grabs text from TextFields and IDs from Dropdowns
    private Object getFieldValue(String fieldName) {
        JComponent comp = fields.get(fieldName);
        String val = "";
        
        if (comp instanceof JTextField) {
            val = ((JTextField) comp).getText().trim();
        } else if (comp instanceof JComboBox<?>) {
            Object sel = ((JComboBox<?>) comp).getSelectedItem();
            if (sel != null && !sel.toString().trim().isEmpty()) {
                val = sel.toString().split(" - ")[0]; // Extract the ID safely
            }
        }
        return val.isEmpty() ? null : val;
    }

    // THE FIX: Checks for values in both components
    private boolean hasAnyValue() {
        for (JComponent comp : fields.values()) {
            if (comp instanceof JTextField) {
                if (!((JTextField) comp).getText().trim().isEmpty()) return true;
            } else if (comp instanceof JComboBox<?>) {
                Object sel = ((JComboBox<?>) comp).getSelectedItem();
                if (sel != null && !sel.toString().trim().isEmpty()) return true;
            }
        }
        return false;
    }

    // THE FIX: Maps clicked table rows back to the correct dropdown selection, using wildcard <?> to silence warnings
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
                String strVal = (val == null) ? "" : String.valueOf(val);
                
                JComponent comp = fields.get(f);
                if (comp instanceof JTextField) {
                    ((JTextField) comp).setText(strVal);
                } else if (comp instanceof JComboBox<?>) {
                    JComboBox<?> cb = (JComboBox<?>) comp;
                    boolean found = false;
                    for (int i = 0; i < cb.getItemCount(); i++) {
                        Object itemObj = cb.getItemAt(i);
                        if (itemObj != null) {
                            String item = itemObj.toString();
                            if (item.startsWith(strVal + " - ") || item.equals(strVal)) {
                                cb.setSelectedIndex(i);
                                found = true; break;
                            }
                        }
                    }
                    if (!found) cb.setSelectedItem(""); // Reset if invalid mapping
                }
            }
        }
    }

    private void insertRecord() {
        if (!hasAnyValue()) {
            UIUtils.error(this, new IllegalArgumentException("Please fill in at least one field before adding a record.")); return;
        }
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(insertSql)) {
            for (int i = 0; i < cfg.fields.length; i++) {
                p.setObject(i + 1, getFieldValue(cfg.fields[i]));
            }
            p.executeUpdate();
            Audit.log(session, "ADD_" + cfg.table, "New record");
            refresh(); clearForm(); UIUtils.info(this, "Record added successfully.");
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void updateRecord() {
        if (id.getText().isEmpty()) { UIUtils.info(this, "Select a record from the table first."); return; }
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(updateSql)) {
            int i;
            for (i = 0; i < cfg.fields.length; i++) {
                p.setObject(i + 1, getFieldValue(cfg.fields[i]));
            }
            p.setObject(i + 1, id.getText().trim());

            int updated = p.executeUpdate();
            if (updated == 0) {
                UIUtils.error(this, new IllegalStateException("No record was updated. It may have been deleted by another user.")); return;
            }
            Audit.log(session, "UPDATE_" + cfg.table, cfg.idColumn + "=" + id.getText());
            refresh(); UIUtils.info(this, "Record updated successfully.");
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void deleteRecord() {
        if (id.getText().isEmpty() || !UIUtils.confirm(this, "Permanently delete selected record?")) return;
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(deleteSql)) {
            p.setString(1, id.getText().trim());
            int deleted = p.executeUpdate();
            if (deleted == 0) {
                UIUtils.error(this, new IllegalStateException("No record was deleted. It may already be gone.")); return;
            }
            Audit.log(session, "DELETE_" + cfg.table, cfg.idColumn + "=" + id.getText());
            refresh(); clearForm();
        } catch (Exception e) { UIUtils.error(this, e); }
    }

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