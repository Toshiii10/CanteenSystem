package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserOrdersPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);

    public UserOrdersPanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;
        
        JPanel headerPanel = new JPanel();
        headerPanel.add(UIUtils.title("My Orders"));
        
        // Removed the old-school Order ID text field and label entirely
        
        JButton btnCancel = new JButton("Cancel Selected Order");
        JButton btnRefresh = new JButton("Refresh");
        
        headerPanel.add(btnCancel);
        headerPanel.add(btnRefresh);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Lock the table for modern Point-and-Click UX
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null); // Prevent accidental cell text editing
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        btnCancel.addActionListener(e -> cancelOrder());
        btnRefresh.addActionListener(e -> loadData());
        
        loadData();
    }

    private void loadData() {
        String sql = """
            SELECT o.order_id, o.order_no, o.ordered_at, o.total_amount, 
                   o.amount_paid, o.balance, o.payment_status, o.order_status 
            FROM orders o 
            JOIN customer_profiles c ON c.customer_id = o.customer_id 
            WHERE c.user_id = ? 
            ORDER BY o.order_id DESC
            """;
        try {
            model.load(sql, session.userId);
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void cancelOrder() {
        // 1. Verify a row is actually highlighted
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            UIUtils.info(this, "Please click on an order in the table first.");
            return;
        }

        // 2. Extract the hidden Order ID from the selected row's first column
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int orderId;
        try {
            orderId = Integer.parseInt(String.valueOf(model.getValueAt(modelRow, 0)));
        } catch (NumberFormatException e) {
            UIUtils.error(this, new IllegalArgumentException("System Error: Could not read the Order ID from the selected row."));
            return;
        }

        // 3. Prevent accidental misclicks
        if (!UIUtils.confirm(this, "Are you sure you want to cancel Order ID " + orderId + "?")) {
            return;
        }

        String cancelSql = """
            UPDATE orders o 
            JOIN customer_profiles cp ON cp.customer_id = o.customer_id 
            SET o.order_status = 'CANCELLED' 
            WHERE o.order_id = ? AND cp.user_id = ? 
              AND o.order_status IN ('PENDING', 'CONFIRMED') 
              AND o.payment_status <> 'PAID'
            """;

        try (Connection c = DB.getConnection(); 
             PreparedStatement p = c.prepareStatement(cancelSql)) {
            
            p.setInt(1, orderId);
            p.setInt(2, session.userId);
            
            if (p.executeUpdate() == 0) {
                throw new SQLException("Cancellation failed. Only unpaid PENDING or CONFIRMED orders can be cancelled.");
            }
            
            Audit.log(session, "CANCEL_ORDER", "Order ID: " + orderId);
            loadData(); // Instantly refresh the grid to show 'CANCELLED' status
            UIUtils.info(this, "Order successfully cancelled.");
            
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
}