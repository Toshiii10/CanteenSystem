package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserDashboardPanel extends JPanel {
    private final UserSession s;
    
    // Core metrics data display components - Scaled up tracking references
    private final JLabel lblStats = new JLabel("Today's Orders: 0 | Today's Expense: PHP 0.0");
    private final JLabel lblCalories = new JLabel("0 kcal");
    private final JLabel lblCarbs = new JLabel("0.0 g");
    private final JLabel lblProtein = new JLabel("0.0 g");
    private final JLabel lblFats = new JLabel("0.0 g");
    private final JLabel lblSugar = new JLabel("0.0 g");
    private final JLabel lblSodium = new JLabel("0.0 g");

    public UserDashboardPanel(UserSession session) {
        this.s = session;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(new Color(248, 249, 250)); 

        // 1. Core Section Content Panel (Uses clean Vertical Grid Alignment)
        JPanel contentGrid = new JPanel();
        contentGrid.setLayout(new BoxLayout(contentGrid, BoxLayout.Y_AXIS));
        contentGrid.setOpaque(false);

        // Section Title - Boosted to 24pt Bold Tahoma
        JLabel lblMainTitle = new JLabel("Customer Portal Dashboard");
        lblMainTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblMainTitle.setForeground(new Color(33, 37, 41));
        contentGrid.add(lblMainTitle);
        contentGrid.add(Box.createVerticalStrut(15));

        // Subtitle Stats Row - Boosted to 16pt Bold Tahoma
        lblStats.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblStats.setForeground(new Color(33, 37, 41));
        contentGrid.add(lblStats);
        
        // Polished under-line divider graphic rule
        JPanel divider = new JPanel();
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2)); // Slightly thicker tracking bar
        divider.setBackground(new Color(173, 181, 189));
        contentGrid.add(Box.createVerticalStrut(8));
        contentGrid.add(divider);
        contentGrid.add(Box.createVerticalStrut(20));

        // Nutritional Section Header Subtitle - Boosted to 18pt Bold Tahoma
        JLabel lblNutriHeader = new JLabel("Daily Nutritional Intake Summary");
        lblNutriHeader.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNutriHeader.setForeground(new Color(40, 167, 69)); 
        contentGrid.add(lblNutriHeader);
        contentGrid.add(Box.createVerticalStrut(20));

        // 2. Build the Nutrition Matrix Field List Grid Block (Expanded width boundary)
        JPanel nutritionPanel = new JPanel(new GridLayout(6, 2, 15, 16));
        nutritionPanel.setOpaque(false);
        nutritionPanel.setMaximumSize(new Dimension(550, 320)); // Made wider and taller to fit bigger text
        nutritionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Setup custom list rows utilizing prominent 15pt Tahoma fonts
        addNutriRow(nutritionPanel, "Estimated Calories:", lblCalories);
        addNutriRow(nutritionPanel, "Carbohydrates:", lblCarbs);
        addNutriRow(nutritionPanel, "Protein:", lblProtein);
        addNutriRow(nutritionPanel, "Fats:", lblFats);
        addNutriRow(nutritionPanel, "Sugar:", lblSugar);
        addNutriRow(nutritionPanel, "Sodium:", lblSodium);

        contentGrid.add(nutritionPanel);
        add(contentGrid, BorderLayout.CENTER);

        // 3. Add an explicit refresh button at the bottom matching the admin dashboard
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 14)); // Scaled button font up to 14pt
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(8, 18, 8, 18) // Increased inner padding button space
        ));
        btnRefresh.addActionListener(e -> loadData());
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initial Data Fetch Pass
        loadData();
    }

    private void addNutriRow(JPanel container, String title, JLabel valueLabel) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 15)); 
        titleLabel.setForeground(new Color(33, 37, 41));
        
        valueLabel.setFont(new Font("Tahoma", Font.PLAIN, 15)); 
        valueLabel.setForeground(new Color(33, 37, 41));
        
        container.add(titleLabel);
        container.add(valueLabel);
    }

    private void loadData() {
        try (Connection c = DB.getConnection()) {
            
            // THE FIX 1: Now strictly counts TODAY'S active orders (Ignores cancelled ones)
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT COUNT(*), COALESCE(SUM(total_amount), 0) FROM orders " +
                "WHERE order_status != 'CANCELLED' " +
                "AND customer_id = (SELECT customer_id FROM customer_profiles WHERE user_id = ?) " +
                "AND DATE(ordered_at) = CURDATE()")) {
                ps.setInt(1, s.userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        lblStats.setText("Today's Orders: " + rs.getInt(1) + " | Today's Expense: PHP " + String.format("%.1f", rs.getDouble(2)));
                    }
                }
            }

            // THE FIX 2: Nutrition strictly counts food the student actually received today
            try (PreparedStatement ps = c.prepareStatement(
                "SELECT COALESCE(SUM(p.calories * oi.quantity), 0), COALESCE(SUM(p.carbohydrates * oi.quantity), 0), " +
                "COALESCE(SUM(p.protein * oi.quantity), 0), COALESCE(SUM(p.fat * oi.quantity), 0), " +
                "COALESCE(SUM(p.sugar * oi.quantity), 0), COALESCE(SUM(p.sodium * oi.quantity), 0) " +
                "FROM order_items oi JOIN orders o ON o.order_id = oi.order_id JOIN products p ON p.product_id = oi.product_id " +
                "WHERE o.customer_id = (SELECT customer_id FROM customer_profiles WHERE user_id = ?) " +
                "AND DATE(o.ordered_at) = CURDATE() " +
                "AND o.order_status IN ('READY', 'SERVED', 'COMPLETED')")) {
                ps.setInt(1, s.userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        lblCalories.setText(rs.getInt(1) + " kcal");
                        lblCarbs.setText(String.format("%.1f g", rs.getDouble(2)));
                        lblProtein.setText(String.format("%.1f g", rs.getDouble(3)));
                        lblFats.setText(String.format("%.1f g", rs.getDouble(4)));
                        lblSugar.setText(String.format("%.1f g", rs.getDouble(5)));
                        lblSodium.setText(String.format("%.1f mg", rs.getDouble(6)));
                    }
                }
            }
        } catch (Exception e) {
            lblStats.setText("Metrics tracking temporarily offline.");
        }
    }
}