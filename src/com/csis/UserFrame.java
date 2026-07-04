package com.csis;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class UserFrame extends JFrame {
    private final UserSession s;

    public UserFrame(UserSession x) {
        super(AppConstants.APP_NAME + " - Customer Portal");
        s = x;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 730);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        };
        headerPanel.setBackground(new Color(250, 250, 250)); 
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 228, 232)), 
            BorderFactory.createEmptyBorder(10, 20, 10, 20) 
        ));
        
        JLabel titleAndWelcomeLabel = new JLabel("Customer Portal Dashboard — Welcome, " + s.fullName + "!");
        titleAndWelcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        titleAndWelcomeLabel.setForeground(new Color(33, 37, 41)); 
        headerPanel.add(titleAndWelcomeLabel, BorderLayout.WEST);
        
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
        add(headerPanel, BorderLayout.NORTH);
        
        JTabbedPane t = new JTabbedPane();
        t.setFont(new Font("Tahoma", Font.PLAIN, 13)); 
        t.setFocusable(false);
        
        t.addTab("Dashboard", new UserDashboardPanel(s));
        t.addTab("Menu / Order", new UserOrderPanel(s));
        t.addTab("My Orders", new UserOrdersPanel(s));
        t.addTab("Ready for Pickup", new UserReadyOrdersPanel(s));
        t.addTab("Payments", new UserPaymentsPanel(s));

        // UPGRADED WALLET INJECTED HERE
        t.addTab("Prepaid Wallet", new UserWalletPanel(s));

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
        applyGlobalTahomaTypography(this);
        
        this.revalidate();
        this.repaint();
    }

    private void applyGlobalTahomaTypography(Component component) {
        if (component instanceof JTableHeader) {
            JTableHeader header = (JTableHeader) component;
            Font currentFont = header.getFont();
            int style = (currentFont != null) ? currentFont.getStyle() : Font.BOLD;
            int size = (currentFont != null) ? currentFont.getSize() : 13;
            header.setFont(new Font("Tahoma", style, size));
        } else {
            Font currentFont = component.getFont();
            if (currentFont != null) {
                component.setFont(new Font("Tahoma", currentFont.getStyle(), currentFont.getSize()));
            }
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyGlobalTahomaTypography(child);
            }
        }
    }
}

class UserWalletPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);

    private final JLabel balanceLabel = new JLabel("PHP 0.00");
    private final JLabel subsidyLabel = new JLabel("PHP 0.00");
    private final JLabel limitLabel = new JLabel("No Limit");
    private final Timer autoRefreshTimer;

    public UserWalletPanel(UserSession session) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.session = session;

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("Prepaid Wallet"), BorderLayout.WEST);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 13));
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        statsPanel.add(createStatCard("Personal Balance", balanceLabel, new Color(40, 167, 69))); 
        statsPanel.add(createStatCard("Subsidy Balance", subsidyLabel, new Color(13, 110, 253))); 
        statsPanel.add(createStatCard("Daily Spending Limit", limitLabel, new Color(220, 53, 69))); 

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(statsPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Transaction History"));
        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());
        loadData();

        autoRefreshTimer = new Timer(15_000, e -> loadData());
        autoRefreshTimer.start();
        addAncestorListener(new AncestorListener() {
            @Override public void ancestorRemoved(AncestorEvent event) { autoRefreshTimer.stop(); }
            @Override public void ancestorAdded(AncestorEvent event) { }
            @Override public void ancestorMoved(AncestorEvent event) { }
        });
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color valueColor) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 5));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        p.setBackground(new Color(248, 249, 250));
        
        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblTitle.setForeground(new Color(108, 117, 125));
        
        valueLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        valueLabel.setForeground(valueColor);
        
        p.add(lblTitle);
        p.add(valueLabel);
        return p;
    }

    private void loadData() {
        try (Connection c = DB.getConnection();
             PreparedStatement p = c.prepareStatement(
                 "SELECT balance, subsidy_balance, daily_limit FROM wallets w JOIN customer_profiles cp ON w.customer_id = cp.customer_id WHERE cp.user_id = ?")) {
            p.setInt(1, session.userId);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    balanceLabel.setText(String.format("PHP %.2f", r.getDouble("balance")));
                    subsidyLabel.setText(String.format("PHP %.2f", r.getDouble("subsidy_balance")));
                    double limit = r.getDouble("daily_limit");
                    limitLabel.setText(limit > 0 ? String.format("PHP %.2f", limit) : "No Limit");
                }
            }
        } catch (Exception e) { UIUtils.error(this, e); }

        try {
            model.load("""
                SELECT txn_id AS 'Txn ID', amount AS 'Amount', txn_type AS 'Type', 
                       reference_order_id AS 'Order Ref', txn_date AS 'Date' 
                FROM wallet_transactions wt 
                JOIN wallets w ON wt.wallet_id = w.wallet_id 
                JOIN customer_profiles cp ON w.customer_id = cp.customer_id 
                WHERE cp.user_id = ? 
                ORDER BY txn_id DESC
                """, session.userId);
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}