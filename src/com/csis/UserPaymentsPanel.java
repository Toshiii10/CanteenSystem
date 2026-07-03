package com.csis;

import javax.swing.*;
import java.awt.*;

public class UserPaymentsPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    
    private final JTextField orderIdField = new JTextField(6);
    private final JTextField amountField = new JTextField(8);
    private final JTextField refField = new JTextField(12);
    private final JTextField proofField = new JTextField(16);
    private final JComboBox<String> methodCombo = new JComboBox<>(new String[]{
        "GCASH", "MAYA", "BANK_TRANSFER", "E_WALLET", "CASH"
    });

    public UserPaymentsPanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;
        
        JPanel headerPanel = new JPanel();
        headerPanel.add(UIUtils.title("Submit Payment"));
        headerPanel.add(new JLabel("Order ID:")); headerPanel.add(orderIdField);
        headerPanel.add(new JLabel("Amount:")); headerPanel.add(amountField);
        headerPanel.add(methodCombo);
        headerPanel.add(new JLabel("Reference:")); headerPanel.add(refField);
        headerPanel.add(new JLabel("Proof File/Link:")); headerPanel.add(proofField);
        
        JButton btnSubmit = new JButton("Submit");
        JButton btnRefresh = new JButton("Refresh");
        
        headerPanel.add(btnSubmit);
        headerPanel.add(btnRefresh);
        
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        btnSubmit.addActionListener(e -> submitPayment());
        btnRefresh.addActionListener(e -> loadData());
        
        loadData();
    }

    private void loadData() {
        String sql = """
            SELECT p.payment_id, p.order_id, p.payment_date, p.amount, 
                   p.method, p.reference_no, p.status, p.remarks 
            FROM payments p 
            JOIN customer_profiles c ON c.customer_id = p.customer_id 
            WHERE c.user_id = ? 
            ORDER BY p.payment_id DESC
            """;
        try {
            model.load(sql, session.userId);
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void submitPayment() {
        if (orderIdField.getText().trim().isEmpty() || amountField.getText().trim().isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Order ID and Amount are required fields."));
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim().replace(",", "")); // Normalize comma inputs
            int customerId = CanteenService.customerId(session.userId);
            String method = String.valueOf(methodCombo.getSelectedItem());
            
            CanteenService.submitPayment(orderId, customerId, amount, method, refField.getText().trim(), proofField.getText().trim());
            
            Audit.log(session, "SUBMIT_PAYMENT", "Order ID: " + orderId);
            loadData();
            
            // Clear fields on success
            orderIdField.setText(""); amountField.setText(""); 
            refField.setText(""); proofField.setText("");
            
            UIUtils.info(this, "Payment submitted to administration for verification.");
            
        } catch (NumberFormatException nfe) {
            UIUtils.error(this, new IllegalArgumentException("Invalid number format in Order ID or Amount field."));
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
}