package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PosPanel extends JPanel {
    private final UserSession s;
    private final QueryTableModel m = new QueryTableModel();
    private final JTable t = new JTable(m);
    private final JTextField product = new JTextField(6);
    private final JTextField qty = new JTextField("1", 5);
    private final JTextField cash = new JTextField(8);

    public PosPanel(UserSession s) {
        super(new BorderLayout(8, 8));
        this.s = s;
        
        JPanel header = new JPanel();
        header.add(UIUtils.title("Point of Sale - Walk-in Sale"));
        header.add(new JLabel("Product ID:")); header.add(product);
        header.add(new JLabel("Qty:")); header.add(qty);
        header.add(new JLabel("Cash:")); header.add(cash);
        
        JButton btnSell = new JButton("Complete Cash Sale");
        JButton btnRefresh = new JButton("Refresh Menu");
        
        header.add(btnSell);
        header.add(btnRefresh);
        
        add(header, BorderLayout.NORTH);
        add(new JScrollPane(t), BorderLayout.CENTER);
        
        btnSell.addActionListener(e -> processSale());
        btnRefresh.addActionListener(e -> loadMenu());
        loadMenu();
    }

    private void loadMenu() {
        try {
            m.load("SELECT product_id, product_name, unit_price, status FROM products ORDER BY product_name");
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void processSale() {
        if (product.getText().trim().isEmpty() || qty.getText().trim().isEmpty() || cash.getText().trim().isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Product ID, Quantity, and Cash amount are required."));
            return;
        }

        try {
            int pid = Integer.parseInt(product.getText().trim());
            int q = Integer.parseInt(qty.getText().trim());
            double received = Double.parseDouble(cash.getText().trim());

            // 1. Create the base order
            int oid = CanteenService.createOrder(null, "WALK_IN", s.userId, "POS cash sale");
            
            // 2. Add item (this updates the database total via trigger/recalc)
            CanteenService.addOrderItem(oid, pid, q, "");

            // 3. Verify total and process payment atomically
            double total = 0.0;
            try (Connection c = DB.getConnection()) {
                c.setAutoCommit(false);
                try {
                    try (PreparedStatement checkTotal = c.prepareStatement("SELECT total_amount FROM orders WHERE order_id = ?")) {
                        checkTotal.setInt(1, oid);
                        try (ResultSet r = checkTotal.executeQuery()) {
                            if (r.next()) total = r.getDouble(1);
                        }
                    }

                    if (received < total) {
                        throw new SQLException("Transaction Declined: Cash received (PHP " + received + ") is less than the total cost (PHP " + total + ").");
                    }

                    try (PreparedStatement payOrder = c.prepareStatement("UPDATE orders SET amount_paid = total_amount, balance = 0, payment_status = 'PAID' WHERE order_id = ?")) {
                        payOrder.setInt(1, oid);
                        payOrder.executeUpdate();
                    }
                    c.commit();
                } catch (SQLException ex) {
                    c.rollback();
                    throw ex;
                }
            }

            // 4. Update logistics routing
            CanteenService.markPreparing(oid, s.userId);
            CanteenService.setOrderStatus(oid, "COMPLETED");
            
            Audit.log(s, "POS_SALE", "Order ID " + oid + " paid PHP " + total);
            UIUtils.info(this, String.format("Sale completed successfully.\nTotal: PHP %.2f\nChange: PHP %.2f", total, (received - total)));
            
            // Reset fields
            product.setText("");
            qty.setText("1");
            cash.setText("");
            
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
}