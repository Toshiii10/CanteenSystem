package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardPanel extends JPanel { 
    // Container array tracking our 6 core analytical metric display cards
    private final StatCard[] cards = new StatCard[6];

    public DashboardPanel(UserSession s) {
        super(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // 1. Setup a Clean Header Ribbon Panel (Redundant Large Title Removed)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBackground(new Color(240, 242, 245));
        btnRefresh.setForeground(new Color(33, 37, 41));
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        
        // Pin the action trigger button to the right side of the panel row boundary
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Initialize a Polished Multi-Column Grid Space
        JPanel gridContentPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridContentPanel.setOpaque(false);

        cards[0] = new StatCard("Available Menu Items", new Color(13, 110, 253));
        cards[1] = new StatCard("Pending Orders", new Color(255, 193, 7));
        cards[2] = new StatCard("Pending Payments", new Color(23, 162, 184));
        cards[3] = new StatCard("Low Stock Ingredients", new Color(220, 53, 69));
        cards[4] = new StatCard("Expiring Batches", new Color(253, 126, 20));
        cards[5] = new StatCard("Today's Gross Sales", new Color(40, 167, 69));

        for (StatCard card : cards) {
            gridContentPanel.add(card);
        }
        add(gridContentPanel, BorderLayout.CENTER);

        // Bind interactive refresh hook triggers
        btnRefresh.addActionListener(e -> load());
        load();
    }

    private String one(Connection c, String sql) throws SQLException {
        try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(sql)) {
            if (r.next() && r.getObject(1) != null) {
                return String.valueOf(r.getObject(1));
            }
            return "0";
        }
    }

    private void load() {
        try (Connection c = DB.getConnection()) {
            cards[0].setValue(one(c, "SELECT COUNT(*) FROM products WHERE status='AVAILABLE'"));
            cards[1].setValue(one(c, "SELECT COUNT(*) FROM orders WHERE order_status='PENDING'"));
            cards[2].setValue(one(c, "SELECT COUNT(*) FROM payments WHERE status='PENDING'"));
            cards[3].setValue(one(c, "SELECT COUNT(*) FROM ingredients WHERE quantity_on_hand <= reorder_level AND status='ACTIVE'"));
            cards[4].setValue(one(c, "SELECT COUNT(*) FROM ingredient_batches WHERE expiry_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) AND status='ACTIVE'"));
            
            double totalSalesValue = Double.parseDouble(one(c, "SELECT COALESCE(SUM(amount_paid), 0) FROM orders WHERE DATE(ordered_at) = CURDATE() AND order_status <> 'CANCELLED'"));
            cards[5].setValue(String.format("PHP %.2f", totalSalesValue));
        } catch (Exception e) {
            for (StatCard card : cards) {
                card.setValue("ERR");
            }
        }
    }

    // Custom Static Class: Draws a clean card box using anti-aliasing and Tahoma text
    private static class StatCard extends JPanel {
        private final JLabel lblValue = new JLabel("0");

        public StatCard(String labelTitle, Color accentColor) {
            super(new BorderLayout(5, 5));
            setBackground(new Color(248, 249, 250));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
            ));

            JLabel lblTitle = new JLabel(labelTitle.toUpperCase());
            lblTitle.setFont(new Font("Tahoma", Font.BOLD, 11));
            lblTitle.setForeground(new Color(108, 117, 125));

            lblValue.setFont(new Font("Tahoma", Font.BOLD, 22));
            lblValue.setForeground(accentColor);

            add(lblTitle, BorderLayout.NORTH);
            add(lblValue, BorderLayout.CENTER);
        }

        public void setValue(String textValue) {
            lblValue.setText(textValue);
        }
    }
}