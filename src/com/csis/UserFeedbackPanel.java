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
    private final JTextArea commentsArea = new JTextArea(4, 20);
    // This panel allows customers to submit feedback for their completed orders.
    // It includes a form for entering feedback details such as Order ID, Rating (1-5), and Comments. 
    // The panel also displays a table of the customer's past feedback submissions, 
    // showing the order ID, rating, comments, status (e.g., "PENDING", "REVIEWED"), and submission date.
    public UserFeedbackPanel(UserSession session) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.session = session;
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("My Feedback"), BorderLayout.WEST);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 13));
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        table.setFont(new Font("Tahoma", Font.PLAIN, 13)); table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Feedback Details"));
        form.setPreferredSize(new Dimension(340, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8); g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;

        int r = 0;
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Order ID:"), g); g.gridx = 1; form.add(orderIdField, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Rating:"), g); g.gridx = 1; form.add(ratingCombo, g);
        g.gridx = 0; g.gridy = ++r; g.gridwidth = 2; form.add(new JLabel("Comments:"), g);
        commentsArea.setLineWrap(true); commentsArea.setWrapStyleWord(true);
        g.gridx = 0; g.gridy = ++r; g.gridwidth = 2; form.add(new JScrollPane(commentsArea), g);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton btnClear = new JButton("Clear"); JButton btnSubmit = new JButton("Submit");
        btnPanel.add(btnClear); btnPanel.add(btnSubmit);
        g.gridx = 0; g.gridy = ++r; g.gridwidth = 2; form.add(btnPanel, g);
        add(form, BorderLayout.EAST);
        
        btnSubmit.addActionListener(e -> submitFeedback()); btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> loadData());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) orderIdField.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
        });
        loadData();
    }

    private void loadData() {
        try { model.load("SELECT f.feedback_id AS 'Feedback ID', f.order_id AS 'Order ID', f.rating AS 'Rating', f.comments AS 'Comments', f.status AS 'Status', f.created_at AS 'Date Submitted' FROM feedback f JOIN customer_profiles cp ON cp.customer_id = f.customer_id WHERE cp.user_id = ? ORDER BY f.feedback_id DESC", session.userId); } catch (Exception ex) { UIUtils.error(this, ex); }
    }

    private void clearForm() { orderIdField.setText(""); commentsArea.setText(""); table.clearSelection(); }

    private void submitFeedback() {
        if (orderIdField.getText().trim().isEmpty()) { UIUtils.error(this, new IllegalArgumentException("Please specify Order ID.")); return; }
        try {
            int orderId = Integer.parseInt(orderIdField.getText().trim()); int customerId = CanteenService.customerId(session.userId);
            try (Connection c = DB.getConnection()) {
                try (PreparedStatement valid = c.prepareStatement("SELECT COUNT(*) FROM orders WHERE order_id = ? AND customer_id = ? AND order_status = 'COMPLETED'")) {
                    valid.setInt(1, orderId); valid.setInt(2, customerId);
                    try (ResultSet result = valid.executeQuery()) { result.next(); if (result.getInt(1) == 0) throw new SQLException("Feedback is only allowed for your own fully COMPLETED orders."); }
                }
                try (PreparedStatement insert = c.prepareStatement("INSERT INTO feedback(customer_id, order_id, rating, comments) VALUES(?, ?, ?, ?)")) {
                    insert.setInt(1, customerId); insert.setInt(2, orderId); insert.setInt(3, Integer.parseInt(String.valueOf(ratingCombo.getSelectedItem()))); insert.setString(4, commentsArea.getText().trim()); insert.executeUpdate();
                }
                Audit.log(session, "SUBMIT_FEEDBACK", "Order ID " + orderId);
                clearForm(); loadData(); UIUtils.info(this, "Feedback submitted successfully.");
            }
        } catch (Exception ex) { UIUtils.error(this, ex); }
    }
}