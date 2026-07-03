package com.csis;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UserFrame extends JFrame {
    private final UserSession s;

    public UserFrame(UserSession x) {
        super(AppConstants.APP_NAME + " - Customer Portal");
        s = x;
        
        // 1. Configure root window specifications and explicit BorderLayout manager
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 730);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        // 2. Build a Highly Polished Top Banner Header Bar Panel
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        };
        headerPanel.setBackground(new Color(250, 250, 250)); // Premium off-white studio tint
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 228, 232)), // Bottom divider line
            BorderFactory.createEmptyBorder(10, 20, 10, 20) // Clean horizontal and vertical margin padding
        ));
        
        // 3. Left-Aligned Text Content (Added directly to WEST position to force horizontal visibility)
        JLabel titleAndWelcomeLabel = new JLabel("Customer Portal Dashboard — Welcome, " + s.fullName + "!");
        titleAndWelcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        titleAndWelcomeLabel.setForeground(new Color(33, 37, 41)); // Clear high-contrast dark text
        headerPanel.add(titleAndWelcomeLabel, BorderLayout.WEST);
        
        // 4. Right-Aligned Logout Action Button (Keeps its Far Right Position)
        JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightWrapper.setOpaque(false);
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnLogout.setBackground(new Color(214, 40, 40)); 
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(186, 24, 24), 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        
        btnLogout.addActionListener(e -> {
            boolean confirmClose = UIUtils.confirm(this, "Are you sure you want to log out of your canteen account?");
            if (confirmClose) {
                Audit.log(s, "LOGOUT", "Customer clicked explicit workspace header logout button");
                dispose(); 
                new AuthFrame().setVisible(true); 
            }
        });
        rightWrapper.add(btnLogout);
        headerPanel.add(rightWrapper, BorderLayout.EAST);
        
        // Attach the layout header panel securely across the northern edge ceiling segment
        add(headerPanel, BorderLayout.NORTH);
        
        // 5. Initialize and load all 12 navigation tabs styled completely in Tahoma
        JTabbedPane t = new JTabbedPane();
        t.setFont(new Font("Tahoma", Font.PLAIN, 13)); 
        t.setFocusable(false);
        
        t.addTab("Dashboard", new UserDashboardPanel(s));
        t.addTab("Menu / Order", new UserOrderPanel(s));
        t.addTab("My Orders", new UserOrdersPanel(s));
        t.addTab("Payments", new UserPaymentsPanel(s));

        t.addTab("Prepaid Wallet", new UserSimplePanel("My Canteen Balance & Transaction History", 
            "SELECT txn_id, amount, txn_type, reference_order_id, txn_date FROM wallet_transactions wt " +
            "JOIN wallets w ON wt.wallet_id = w.wallet_id JOIN customer_profiles cp ON w.customer_id = cp.customer_id " +
            "WHERE cp.user_id = ? ORDER BY txn_id DESC", s.userId));

        t.addTab("Meal Subscriptions", new UserSimplePanel("My Active Meal Subscription Credits", 
            "SELECT plan_name, plan_type, start_date, end_date, credits_total, credits_used, credits_remaining, actual_status " +
            "FROM v_subscription_credits WHERE customer_id = (SELECT customer_id FROM customer_profiles WHERE user_id = ?)", s.userId));

        t.addTab("Announcements", new UserSimplePanel("Announcements", 
            "SELECT title, body, created_at FROM announcements WHERE status='ACTIVE' AND target_role IN ('ALL','CUSTOMER') ORDER BY created_at DESC"));
        
        t.addTab("Notifications", new UserSimplePanel("My Notifications", 
            "SELECT title, body, status, created_at FROM notifications WHERE user_id=? ORDER BY created_at DESC", s.userId));
        
        t.addTab("Messages", new UserMessagePanel(s));
        
        t.addTab("Order Items", new UserSimplePanel("My Order Items", 
            "SELECT o.order_no, p.product_name, oi.quantity, oi.unit_price, oi.line_total, oi.special_instruction FROM order_items oi JOIN orders o ON o.order_id=oi.order_id JOIN products p ON p.product_id=oi.product_id JOIN customer_profiles cp ON cp.customer_id=o.customer_id WHERE cp.user_id=? ORDER BY oi.order_item_id DESC", s.userId));
        
        t.addTab("Feedback", new UserFeedbackPanel(s));
        t.addTab("Profile", new UserProfilePanel(s));
        
        add(t, BorderLayout.CENTER);
        
        // =====================================================================
        // CORE UPGRADE: Run the global Tahoma typography injector pass
        // =====================================================================
        applyGlobalTahomaTypography(this);
        
        this.revalidate();
        this.repaint();
    }

    /**
     * Recursively traverses the entire UI component tree of the frame and forces 
     * every inner element (labels, text fields, tables, headers, lists) to use Tahoma.
     */
    private void applyGlobalTahomaTypography(Component component) {
        // 1. Target table headers explicitly
        if (component instanceof JTableHeader) {
            JTableHeader header = (JTableHeader) component;
            Font currentFont = header.getFont();
            int style = (currentFont != null) ? currentFont.getStyle() : Font.BOLD;
            int size = (currentFont != null) ? currentFont.getSize() : 13;
            header.setFont(new Font("Tahoma", style, size));
        } 
        // 2. Target tables, inputs, text components, buttons, and labels
        else {
            Font currentFont = component.getFont();
            if (currentFont != null) {
                // Keep the component's original size and weight (Plain vs Bold), just swap the family to Tahoma
                component.setFont(new Font("Tahoma", currentFont.getStyle(), currentFont.getSize()));
            }
        }

        // 3. If the component contains sub-elements, drill down into them automatically
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyGlobalTahomaTypography(child);
            }
        }
    }
}