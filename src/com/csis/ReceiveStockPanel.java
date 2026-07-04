package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReceiveStockPanel extends JPanel {
    private final UserSession session;
    private final JComboBox<String> ingredientCombo = new JComboBox<>();
    private final JComboBox<String> supplierCombo = new JComboBox<>();
    private final JTextField batchNumberField = new JTextField(10);
    private final JTextField quantityField = new JTextField(8);
    private final JTextField costField = new JTextField(8);
    private final JTextField expiryDateField = new JTextField(10);
    private final JTextField searchField = new JTextField(18);
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);

    public ReceiveStockPanel(UserSession session) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.session = session;
        
        // HEADER (NORTH)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("Receive Stock"), BorderLayout.WEST);

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchContainer.add(new JLabel("Search Batch:")); searchContainer.add(searchField);
        JButton btnSearch = new JButton("Search"); JButton btnRefresh = new JButton("Refresh");
        searchContainer.add(btnSearch); searchContainer.add(btnRefresh);
        headerPanel.add(searchContainer, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // TABLE (CENTER)
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // FORM (EAST)
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Stock Receipt Details"));
        form.setPreferredSize(new Dimension(340, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8); g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;

        int r = 0;
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Ingredient:"), g); g.gridx = 1; form.add(ingredientCombo, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Supplier:"), g); g.gridx = 1; form.add(supplierCombo, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Batch No:"), g); g.gridx = 1; form.add(batchNumberField, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Qty:"), g); g.gridx = 1; form.add(quantityField, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Unit Cost:"), g); g.gridx = 1; form.add(costField, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Expiry (YYYY-MM-DD):"), g); g.gridx = 1; form.add(expiryDateField, g);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton btnReceive = new JButton("Receive Stock"); JButton btnClear = new JButton("Clear");
        btnPanel.add(btnClear); btnPanel.add(btnReceive);
        g.gridx = 0; g.gridy = ++r; g.gridwidth = 2; form.add(btnPanel, g);
        add(form, BorderLayout.EAST);
        
        // Listeners
        btnSearch.addActionListener(e -> doSearch()); searchField.addActionListener(e -> doSearch());
        btnRefresh.addActionListener(e -> loadData()); btnClear.addActionListener(e -> clearForm());
        btnReceive.addActionListener(e -> processStockReceipt());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                String selectedIngId = String.valueOf(table.getValueAt(row, 1));
                String selectedSupId = String.valueOf(table.getValueAt(row, 2));
                for(int i = 0; i < ingredientCombo.getItemCount(); i++) if(ingredientCombo.getItemAt(i).startsWith(selectedIngId + " - ")) { ingredientCombo.setSelectedIndex(i); break; }
                for(int i = 0; i < supplierCombo.getItemCount(); i++) if(supplierCombo.getItemAt(i).startsWith(selectedSupId + " - ")) { supplierCombo.setSelectedIndex(i); break; }
                batchNumberField.setText(String.valueOf(table.getValueAt(row, 3)));
                Object expiry = table.getValueAt(row, 5); expiryDateField.setText(expiry == null ? "" : String.valueOf(expiry));
                quantityField.setText(String.valueOf(table.getValueAt(row, 6))); costField.setText(String.valueOf(table.getValueAt(row, 7)));
            }
        });
        
        loadDropdowns(); loadData();
    }

    private void loadDropdowns() {
        try (Connection c = DB.getConnection(); Statement s = c.createStatement()) {
            ResultSet rsI = s.executeQuery("SELECT ingredient_id, ingredient_name FROM ingredients");
            while(rsI.next()) ingredientCombo.addItem(rsI.getInt(1) + " - " + rsI.getString(2));
            ResultSet rsS = s.executeQuery("SELECT supplier_id, supplier_name FROM suppliers");
            while(rsS.next()) supplierCombo.addItem(rsS.getInt(1) + " - " + rsS.getString(2));
        } catch(Exception e) { UIUtils.error(this, e); }
    }

    private void loadData() {
        try { model.load("SELECT batch_id, ingredient_id, supplier_id, batch_number, received_date, expiry_date, quantity_available, unit_cost, status FROM ingredient_batches ORDER BY batch_id DESC"); } catch (Exception e) { UIUtils.error(this, e); }
    }
    
    private void doSearch() {
        String term = "%" + searchField.getText().trim() + "%";
        try { model.load("SELECT batch_id, ingredient_id, supplier_id, batch_number, received_date, expiry_date, quantity_available, unit_cost, status FROM ingredient_batches WHERE batch_number LIKE ? ORDER BY batch_id DESC", term); } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void clearForm() {
        batchNumberField.setText(""); quantityField.setText(""); costField.setText(""); expiryDateField.setText(""); table.clearSelection();
    }
    // Validates the input fields, inserts a new stock receipt record into the database, 
    // updates the ingredient's quantity on hand, and logs the stock movement. It also handles transaction management to ensure data integrity.
    private void processStockReceipt() {
        if (ingredientCombo.getSelectedItem() == null || supplierCombo.getSelectedItem() == null || batchNumberField.getText().trim().isEmpty() || quantityField.getText().trim().isEmpty() || costField.getText().trim().isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("All fields except Expiry Date are mandatory.")); return;
        }
        int ingredientId, supplierId; double quantity, unitCost;
        try {
            ingredientId = Integer.parseInt(String.valueOf(ingredientCombo.getSelectedItem()).split(" - ")[0]);
            supplierId = Integer.parseInt(String.valueOf(supplierCombo.getSelectedItem()).split(" - ")[0]);
            quantity = Double.parseDouble(quantityField.getText().trim().replace(",", ""));
            unitCost = Double.parseDouble(costField.getText().trim().replace(",", ""));
            if (quantity <= 0 || unitCost < 0) { UIUtils.error(this, new IllegalArgumentException("Quantity must be > 0, and cost cannot be negative.")); return; }
        } catch (NumberFormatException nfe) { UIUtils.error(this, new IllegalArgumentException("Invalid numeric format in Quantity or Cost fields.")); return; }

        String expiryDate = expiryDateField.getText().trim();
        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false); 
            try (PreparedStatement pBatch = c.prepareStatement("INSERT INTO ingredient_batches(ingredient_id, supplier_id, batch_number, received_date, expiry_date, quantity_received, quantity_available, unit_cost) VALUES(?, ?, ?, CURDATE(), ?, ?, ?, ?)");
                 PreparedStatement pUpdate = c.prepareStatement("UPDATE ingredients SET quantity_on_hand = quantity_on_hand + ? WHERE ingredient_id = ?");
                 PreparedStatement pMove = c.prepareStatement("INSERT INTO stock_movements(ingredient_id, movement_type, quantity_change, remarks, created_by) VALUES(?, 'PURCHASE', ?, 'Received stock', ?)")) {
                
                pBatch.setInt(1, ingredientId); pBatch.setInt(2, supplierId); pBatch.setString(3, batchNumberField.getText().trim());
                if (expiryDate.isEmpty()) pBatch.setNull(4, Types.DATE); else pBatch.setString(4, expiryDate);
                pBatch.setDouble(5, quantity); pBatch.setDouble(6, quantity); pBatch.setDouble(7, unitCost); pBatch.executeUpdate();

                pUpdate.setDouble(1, quantity); pUpdate.setInt(2, ingredientId); pUpdate.executeUpdate();
                pMove.setInt(1, ingredientId); pMove.setDouble(2, quantity); pMove.setInt(3, session.userId); pMove.executeUpdate();

                c.commit(); Audit.log(session, "RECEIVE_STOCK", batchNumberField.getText().trim());
                UIUtils.info(this, "Stock received successfully."); clearForm(); loadData();
            } catch (SQLException e) { c.rollback(); throw e; }
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}