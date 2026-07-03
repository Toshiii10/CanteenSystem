package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReceiveStockPanel extends JPanel {
    private final UserSession session;
    
    // Decompressed UI components with clear naming conventions
    private final JTextField ingredientIdField = new JTextField(5);
    private final JTextField supplierIdField = new JTextField(5);
    private final JTextField batchNumberField = new JTextField(10);
    private final JTextField quantityField = new JTextField(8);
    private final JTextField costField = new JTextField(8);
    private final JTextField expiryDateField = new JTextField(10);
    
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);

    public ReceiveStockPanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;
        
        JPanel headerPanel = new JPanel();
        headerPanel.add(UIUtils.title("Receive Ingredient Stock"));
        headerPanel.add(new JLabel("Ingredient ID:")); headerPanel.add(ingredientIdField);
        headerPanel.add(new JLabel("Supplier ID:")); headerPanel.add(supplierIdField);
        headerPanel.add(new JLabel("Batch No:")); headerPanel.add(batchNumberField);
        headerPanel.add(new JLabel("Qty:")); headerPanel.add(quantityField);
        headerPanel.add(new JLabel("Unit Cost:")); headerPanel.add(costField);
        headerPanel.add(new JLabel("Expiry (YYYY-MM-DD):")); headerPanel.add(expiryDateField);
        
        JButton btnReceive = new JButton("Receive Stock");
        headerPanel.add(btnReceive);
        
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        btnReceive.addActionListener(e -> processStockReceipt());
        loadData();
    }

    private void loadData() {
        String sql = """
            SELECT batch_id, ingredient_id, batch_number, received_date, 
                   expiry_date, quantity_available, unit_cost, status 
            FROM ingredient_batches 
            ORDER BY batch_id DESC
            """;
        try {
            model.load(sql);
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void processStockReceipt() {
        // 1. Defensive Validation: Catch empty strings before hitting parsing logic
        if (ingredientIdField.getText().trim().isEmpty() || 
            supplierIdField.getText().trim().isEmpty() || 
            batchNumberField.getText().trim().isEmpty() || 
            quantityField.getText().trim().isEmpty() || 
            costField.getText().trim().isEmpty()) {
            
            UIUtils.error(this, new IllegalArgumentException("All fields except Expiry Date are mandatory."));
            return;
        }

        int ingredientId;
        int supplierId;
        double quantity;
        double unitCost;

        // 2. Safe Parsing: Catch invalid characters (letters/commas in number fields)
        try {
            ingredientId = Integer.parseInt(ingredientIdField.getText().trim());
            supplierId = Integer.parseInt(supplierIdField.getText().trim());
            quantity = Double.parseDouble(quantityField.getText().trim().replace(",", ""));
            unitCost = Double.parseDouble(costField.getText().trim().replace(",", ""));
            
            if (quantity <= 0 || unitCost < 0) {
                UIUtils.error(this, new IllegalArgumentException("Quantity must be > 0, and cost cannot be negative."));
                return;
            }
        } catch (NumberFormatException nfe) {
            UIUtils.error(this, new IllegalArgumentException("Invalid numeric format in ID, Quantity, or Cost fields."));
            return;
        }

        String expiryDate = expiryDateField.getText().trim();

        // Database text blocks for maintainability
        String insertBatchSql = """
            INSERT INTO ingredient_batches(ingredient_id, supplier_id, batch_number, 
                                           received_date, expiry_date, quantity_received, 
                                           quantity_available, unit_cost) 
            VALUES(?, ?, ?, CURDATE(), ?, ?, ?, ?)
            """;
        String updateInvSql = "UPDATE ingredients SET quantity_on_hand = quantity_on_hand + ? WHERE ingredient_id = ?";
        String insertMoveSql = """
            INSERT INTO stock_movements(ingredient_id, movement_type, quantity_change, remarks, created_by) 
            VALUES(?, 'PURCHASE', ?, 'Received stock', ?)
            """;

        // 3. Strict Try-With-Resources Transaction Block
        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false); // Lock the transaction state
            
            try (PreparedStatement pBatch = c.prepareStatement(insertBatchSql);
                 PreparedStatement pUpdate = c.prepareStatement(updateInvSql);
                 PreparedStatement pMove = c.prepareStatement(insertMoveSql)) {
                
                // Route 1: Insert New Batch
                pBatch.setInt(1, ingredientId);
                pBatch.setInt(2, supplierId);
                pBatch.setString(3, batchNumberField.getText().trim());
                if (expiryDate.isEmpty()) {
                    pBatch.setNull(4, Types.DATE);
                } else {
                    pBatch.setString(4, expiryDate);
                }
                pBatch.setDouble(5, quantity);
                pBatch.setDouble(6, quantity);
                pBatch.setDouble(7, unitCost);
                pBatch.executeUpdate();

                // Route 2: Update Master Inventory Levels
                pUpdate.setDouble(1, quantity);
                pUpdate.setInt(2, ingredientId);
                int updatedRows = pUpdate.executeUpdate();
                if (updatedRows == 0) {
                    throw new SQLException("Transaction aborted: Ingredient ID " + ingredientId + " does not exist.");
                }

                // Route 3: Log Stock Movement to Audit Ledger
                pMove.setInt(1, ingredientId);
                pMove.setDouble(2, quantity);
                pMove.setInt(3, session.userId);
                pMove.executeUpdate();

                c.commit(); // Seal the transaction
                
                Audit.log(session, "RECEIVE_STOCK", batchNumberField.getText().trim());
                UIUtils.info(this, "Stock strictly received, verified, and logged.");
                
                // Clear the runway on success
                ingredientIdField.setText(""); supplierIdField.setText("");
                batchNumberField.setText(""); quantityField.setText("");
                costField.setText(""); expiryDateField.setText("");
                
                loadData();
                
            } catch (SQLException e) {
                c.rollback(); // Safe, guaranteed rollback because connection 'c' is alive
                throw e; // Pass to outer UI handler
            }
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
}