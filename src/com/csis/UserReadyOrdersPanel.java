package com.csis;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;

public class UserReadyOrdersPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    private final JLabel statusLabel = new JLabel(" ");
    private final Timer autoRefreshTimer;
    // This panel shows the logged-in customer's orders that are currently marked as "READY" for pickup. 
    // It auto-refreshes every 10 seconds to keep the information up-to-date without requiring manual refreshes.
    public UserReadyOrdersPanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("Ready for Pickup"), BorderLayout.WEST);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(7, 0, 0, 0), 
            btnRefresh.getBorder()
        ));
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        statusLabel.setForeground(new Color(40, 167, 69));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        add(statusLabel, BorderLayout.SOUTH);

        table.setFont(new Font("Tahoma", Font.PLAIN, 13)); 
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));

        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());
        loadData();

        autoRefreshTimer = new Timer(10_000, e -> loadData());
        autoRefreshTimer.start();

        addAncestorListener(new AncestorListener() {
            @Override public void ancestorRemoved(AncestorEvent event) { autoRefreshTimer.stop(); }
            @Override public void ancestorAdded(AncestorEvent event) { }
            @Override public void ancestorMoved(AncestorEvent event) { }
        });
    }
    // Loads the ready orders for the logged-in user and updates the status message accordingly.
    private void loadData() {
        String sql = """
                SELECT o.order_id AS 'Order ID', o.order_no AS 'Order No', o.order_type AS 'Type', 
                       o.total_amount AS 'Total Amount', o.prep_ready_at AS 'Time Ready'
                FROM orders o
                JOIN customer_profiles c ON c.customer_id = o.customer_id
                WHERE c.user_id = ? AND o.order_status = 'READY'
                ORDER BY o.prep_ready_at ASC
                """;
        try {
            model.load(sql, session.userId);
            if (model.getRowCount() > 0) {
                statusLabel.setText("You have " + model.getRowCount() + " order(s) ready for pickup. Please proceed to the counter!");
            } else {
                statusLabel.setText("No orders are currently ready for pickup.");
            }
        } catch (Exception e) { 
            UIUtils.error(this, e); 
        }
    }
}