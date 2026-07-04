package com.csis;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.sql.*;

public class UserOrdersPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    private final JTextField orderIdField = new JTextField(6);
    private final Timer autoRefreshTimer;

    public UserOrdersPanel(UserSession session) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.session = session;
        
        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("My Orders"), BorderLayout.WEST);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 13));
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // TABLE
        table.setFont(new Font("Tahoma", Font.PLAIN, 13)); table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // FORM
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Order Management"));
        form.setPreferredSize(new Dimension(320, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8); g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;

        orderIdField.setEditable(false); orderIdField.setBackground(new Color(240, 240, 240));
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Order ID:"), g); g.gridx = 1; form.add(orderIdField, g);

        JButton btnCancel = new JButton("Cancel Pending Order");
        g.gridx = 0; g.gridy = 1; g.gridwidth = 2; form.add(btnCancel, g);
        add(form, BorderLayout.EAST);
        
        btnCancel.addActionListener(e -> cancelOrder()); btnRefresh.addActionListener(e -> loadData());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) orderIdField.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
        });
        
        loadData();
        autoRefreshTimer = new Timer(15_000, e -> loadData()); autoRefreshTimer.start();
        addAncestorListener(new AncestorListener() {
            @Override public void ancestorRemoved(AncestorEvent event) { autoRefreshTimer.stop(); }
            @Override public void ancestorAdded(AncestorEvent event) { }
            @Override public void ancestorMoved(AncestorEvent event) { }
        });
    }

    private void loadData() {
        try { model.load("SELECT o.order_id, o.order_no, o.ordered_at, o.total_amount, o.amount_paid, o.balance, o.payment_status, o.order_status FROM orders o JOIN customer_profiles c ON c.customer_id = o.customer_id WHERE c.user_id = ? ORDER BY o.order_id DESC", session.userId); } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void cancelOrder() {
        if (orderIdField.getText().trim().isEmpty()) { UIUtils.error(this, new IllegalArgumentException("Enter an Order ID to cancel.")); return; }
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement("UPDATE orders o JOIN customer_profiles cp ON cp.customer_id = o.customer_id SET o.order_status = 'CANCELLED' WHERE o.order_id = ? AND cp.user_id = ? AND o.order_status IN ('PENDING', 'CONFIRMED') AND o.payment_status <> 'PAID'")) {
            int orderId = Integer.parseInt(orderIdField.getText().trim()); p.setInt(1, orderId); p.setInt(2, session.userId);
            if (p.executeUpdate() == 0) throw new SQLException("Cancellation failed. Only unpaid PENDING/CONFIRMED orders can be cancelled.");
            Audit.log(session, "CANCEL_ORDER", "Order ID: " + orderId); loadData(); orderIdField.setText(""); table.clearSelection(); UIUtils.info(this, "Order successfully cancelled.");
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}