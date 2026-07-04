package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PosPanel extends JPanel {
    private final UserSession s;
    private final QueryTableModel m = new QueryTableModel();
    private final JTable t = new JTable(m);
    private final JTextField searchField = new JTextField(18);
    private final JTextField product = new JTextField(6);
    private final JTextField qty = new JTextField("1", 5);
    private final JTextField cash = new JTextField(8);

    public PosPanel(UserSession s) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.s = s;
        
        // HEADER (NORTH)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("Point of Sale - Walk-in Sale"), BorderLayout.WEST);

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchContainer.add(new JLabel("Search Product:")); searchContainer.add(searchField);
        JButton btnSearch = new JButton("Search"); JButton btnRefresh = new JButton("Refresh");
        searchContainer.add(btnSearch); searchContainer.add(btnRefresh);
        headerPanel.add(searchContainer, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // TABLE (CENTER)
        t.setFont(new Font("Tahoma", Font.PLAIN, 13)); t.setRowHeight(25);
        t.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 13));
        add(new JScrollPane(t), BorderLayout.CENTER);

        // FORM (EAST)
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Transaction Details"));
        form.setPreferredSize(new Dimension(320, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8); g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;

        int r = 0;
        product.setEditable(false); product.setBackground(new Color(240, 240, 240));
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Product ID:"), g); g.gridx = 1; form.add(product, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Qty:"), g); g.gridx = 1; form.add(qty, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Cash Received:"), g); g.gridx = 1; form.add(cash, g);

        JButton btnSell = new JButton("Complete Cash Sale");
        JButton btnClear = new JButton("Clear");
        btnSell.setFont(btnSell.getFont().deriveFont(Font.PLAIN, 12f));
        btnClear.setFont(btnClear.getFont().deriveFont(Font.PLAIN, 12f));
        btnClear.setPreferredSize(new Dimension(100, 36));
        btnSell.setPreferredSize(new Dimension(200, 42));
        JPanel btnPanel = new JPanel(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        b.insets = new Insets(0, 0, 0, 0);
        b.fill = GridBagConstraints.HORIZONTAL;
        b.gridy = 0;
        b.gridx = 0; b.weightx = 0.35; btnPanel.add(btnClear, b);
        b.gridx = 1; b.weightx = 0.65; btnPanel.add(btnSell, b);
        g.gridx = 0; g.gridy = ++r; g.gridwidth = 2; form.add(btnPanel, g);
        add(form, BorderLayout.EAST);
        
        // Listeners
        btnSearch.addActionListener(e -> doSearch()); searchField.addActionListener(e -> doSearch());
        btnRefresh.addActionListener(e -> loadMenu()); btnClear.addActionListener(e -> clearForm());
        btnSell.addActionListener(e -> processSale());
        
        t.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && t.getSelectedRow() >= 0) {
                product.setText(String.valueOf(t.getValueAt(t.getSelectedRow(), 0))); qty.setText("1");
            }
        });
        loadMenu();
    }

    private void loadMenu() {
        try { m.load("SELECT product_id, product_name, unit_price, status FROM products ORDER BY product_name"); } catch (Exception e) { UIUtils.error(this, e); }
    }
    
    private void doSearch() {
        String term = "%" + searchField.getText().trim() + "%";
        try { m.load("SELECT product_id, product_name, unit_price, status FROM products WHERE product_name LIKE ? ORDER BY product_name", term); } catch (Exception e) { UIUtils.error(this, e); }
    }
    
    private void clearForm() { product.setText(""); qty.setText("1"); cash.setText(""); t.clearSelection(); }

    private void processSale() {
        if (product.getText().trim().isEmpty() || qty.getText().trim().isEmpty() || cash.getText().trim().isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Product ID, Quantity, and Cash amount are required.")); return;
        }
        try {
            int pid = Integer.parseInt(product.getText().trim()); int q = Integer.parseInt(qty.getText().trim()); double received = Double.parseDouble(cash.getText().trim());
            int oid = CanteenService.createOrder(null, "WALK_IN", s.userId, "POS cash sale");
            CanteenService.addOrderItem(oid, pid, q, "");
            double total = 0.0;
            try (Connection c = DB.getConnection()) {
                c.setAutoCommit(false);
                try {
                    try (PreparedStatement checkTotal = c.prepareStatement("SELECT total_amount FROM orders WHERE order_id = ?")) {
                        checkTotal.setInt(1, oid); try (ResultSet r = checkTotal.executeQuery()) { if (r.next()) total = r.getDouble(1); }
                    }
                    if (received < total) throw new SQLException("Transaction Declined: Cash received (PHP " + received + ") is less than the total cost (PHP " + total + ").");
                    try (PreparedStatement payOrder = c.prepareStatement("UPDATE orders SET amount_paid = total_amount, balance = 0, payment_status = 'PAID' WHERE order_id = ?")) {
                        payOrder.setInt(1, oid); payOrder.executeUpdate();
                    }
                    c.commit();
                } catch (SQLException ex) { c.rollback(); throw ex; }
            }
            CanteenService.markPreparing(oid, s.userId); CanteenService.setOrderStatus(oid, "COMPLETED");
            Audit.log(s, "POS_SALE", "Order ID " + oid + " paid PHP " + total);
            UIUtils.info(this, String.format("Sale completed successfully.\nTotal: PHP %.2f\nChange: PHP %.2f", total, (received - total)));
            clearForm();
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}