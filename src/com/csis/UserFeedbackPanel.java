package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserFeedbackPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    
    private final JTextField orderIdField = new JTextField(7);
    private final JComboBox<String> ratingCombo = new JComboBox<>(new String[]{"5", "4", "3", "2", "1"});
    private final JTextArea commentsArea = new JTextArea(3, 30);

    public UserFeedbackPanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;
        
        JPanel formPanel = new JPanel();
        formPanel.add(UIUtils.title("My Feedback"));
        formPanel.add(new JLabel("Completed Order ID:")); 
        formPanel.add(orderIdField);
        formPanel.add(new JLabel("Rating:")); 
        formPanel.add(ratingCombo);
        formPanel.add(new JLabel("Comments:")); 
        formPanel.add(new JScrollPane(commentsArea));
        
        JButton btnSend = new JButton("Submit Feedback");
        JButton btnRefresh = new JButton("Refresh");
        
        formPanel.add(btnSend);
        formPanel.add(btnRefresh);
        
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        btnSend.addActionListener(e -> submitFeedback());
        btnRefresh.addActionListener(e -> loadData());
        
        loadData();
    }

    private void loadData() {
        String sql = """
            SELECT f.feedback_id, f.order_id, f.rating, f.comments, f.status, f.created_at 
            FROM feedback f 
            JOIN customer_profiles cp ON cp.customer_id = f.customer_id 
            WHERE cp.user_id = ? 
            ORDER BY f.feedback_id DESC
            """;
        try {
            model.load(sql, session.userId);
        } catch (Exception ex) {
            UIUtils.error(this, ex);
        }
    }

    private void submitFeedback() {
        String orderText = orderIdField.getText().trim();
        if (orderText.isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Please specify the Order ID you are reviewing."));
            return;
        }

        try {
            int orderId = Integer.parseInt(orderText);
            int customerId = CanteenService.customerId(session.userId);
            
            try (Connection c = DB.getConnection()) {
                // 1. Validate that the order exists, belongs to the user, and is fully completed
                String checkSql = "SELECT COUNT(*) FROM orders WHERE order_id = ? AND customer_id = ? AND order_status = 'COMPLETED'";
                try (PreparedStatement valid = c.prepareStatement(checkSql)) {
                    valid.setInt(1, orderId);
                    valid.setInt(2, customerId);
                    try (ResultSet result = valid.executeQuery()) {
                        result.next();
                        if (result.getInt(1) == 0) {
                            throw new SQLException("Feedback is only allowed for your own fully COMPLETED orders.");
                        }
                    }
                }
                
                // 2. Insert the feedback securely
                String insertSql = "INSERT INTO feedback(customer_id, order_id, rating, comments) VALUES(?, ?, ?, ?)";
                try (PreparedStatement insert = c.prepareStatement(insertSql)) {
                    insert.setInt(1, customerId);
                    insert.setInt(2, orderId);
                    insert.setInt(3, Integer.parseInt(String.valueOf(ratingCombo.getSelectedItem())));
                    insert.setString(4, commentsArea.getText().trim());
                    insert.executeUpdate();
                }
                
                Audit.log(session, "SUBMIT_FEEDBACK", "Order ID " + orderId);
                
                // Reset form fields
                orderIdField.setText("");
                commentsArea.setText("");
                
                loadData();
                UIUtils.info(this, "Thank you! Your feedback has been submitted successfully.");
            }
        } catch (NumberFormatException nfe) {
            UIUtils.error(this, new IllegalArgumentException("Order ID must be a valid number."));
        } catch (Exception ex) {
            UIUtils.error(this, ex);
        }
    }
}