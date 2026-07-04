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
        setSize(1450, 730);
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

        t.addTab("Meal Subscriptions", new UserSubscriptionPanel(s));

        t.addTab("Announcements", new UserSimplePanel("Announcements", 
            "SELECT title, body, created_at FROM announcements WHERE status='ACTIVE' AND target_role IN ('ALL','CUSTOMER') ORDER BY created_at DESC"));
        
        t.addTab("Notifications", new UserSimplePanel("My Notifications", 
            "SELECT notification_id, title, body, status, created_at FROM notifications WHERE user_id=? ORDER BY created_at DESC", s.userId));
        
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
        
        // --- THE FIX: Renamed button to be inclusive of Bank Transfer ---
        JButton btnTopUp = new JButton("Top-Up Wallet");
        btnTopUp.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnTopUp.setBackground(new Color(0, 123, 255));
        btnTopUp.setForeground(Color.WHITE);
        btnTopUp.setFocusPainted(false);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 13));
        
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.add(btnTopUp);
        rightButtons.add(btnRefresh);
        
        headerPanel.add(rightButtons, BorderLayout.EAST);

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

        btnTopUp.addActionListener(e -> showTopUpDialog());
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

    
    // --- NEW: TOP-UP METHOD ---
    private void showTopUpDialog() {
        JComboBox<String> methodCombo = new JComboBox<>(new String[]{"GCash / Maya", "Bank Transfer"});
        JTextField amountField = new JTextField("100", 10);
        JTextField phoneField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField refField = new JTextField(15);
        
        // Disabled by default because GCash is selected first
        nameField.setEnabled(false); 

        // Dynamic unlocking logic
        methodCombo.addActionListener(e -> {
            boolean isGcash = methodCombo.getSelectedIndex() == 0;
            phoneField.setEnabled(isGcash);
            nameField.setEnabled(!isGcash);
            
            // Auto-clear the inactive field
            if (isGcash) nameField.setText("");
            else phoneField.setText("");
        });

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Payment Method:")); panel.add(methodCombo);
        panel.add(new JLabel("Amount to Load (PHP):")); panel.add(amountField);
        panel.add(new JLabel("Phone No. (GCash/Maya):")); panel.add(phoneField);
        panel.add(new JLabel("Account Name (Bank):")); panel.add(nameField);
        panel.add(new JLabel("Reference No.:")); panel.add(refField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Digital Wallet Top-Up", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText().trim().replace(",", ""));
                if (amount < 50) throw new IllegalArgumentException("Minimum top-up amount is PHP 50.00");
                
                boolean isGcash = methodCombo.getSelectedIndex() == 0;
                String phone = phoneField.getText().trim();
                String name = nameField.getText().trim();
                String ref = refField.getText().trim();

                // Strict tracking validation
                if (isGcash && phone.isEmpty()) throw new IllegalArgumentException("Phone Number is required for GCash/Maya.");
                if (!isGcash && name.isEmpty()) throw new IllegalArgumentException("Account Name is required for Bank Transfer.");
                if (ref.isEmpty()) throw new IllegalArgumentException("Reference number is required to verify the transaction.");

                int cid = CanteenService.customerId(session.userId);
                
                try (Connection c = DB.getConnection()) {
                    c.setAutoCommit(false);
                    try {
                        int wId = -1;
                        try (PreparedStatement getW = c.prepareStatement("SELECT wallet_id FROM wallets WHERE customer_id = ? FOR UPDATE")) {
                            getW.setInt(1, cid);
                            ResultSet rs = getW.executeQuery();
                            if (rs.next()) wId = rs.getInt("wallet_id");
                            else throw new SQLException("Wallet not activated. Please ask the Admin to register you.");
                        }

                        // Add money to wallet
                        try (PreparedStatement upd = c.prepareStatement("UPDATE wallets SET balance = balance + ? WHERE wallet_id = ?")) {
                            upd.setDouble(1, amount);
                            upd.setInt(2, wId);
                            upd.executeUpdate();
                        }

                        // Build a beautiful description for the Admin Ledger
                        String methodStr = isGcash ? "GCash" : "Bank Transfer";
                        String details = isGcash ? ("Phone: " + phone) : ("Name: " + name);
                        String ledgerDescription = "Top-Up via " + methodStr + " (" + details + " | Ref: " + ref + ")";

                        // Log it in the transaction history
                        try (PreparedStatement log = c.prepareStatement("INSERT INTO wallet_transactions (wallet_id, amount, txn_type, description) VALUES (?, ?, 'LOAD', ?)")) {
                            log.setInt(1, wId);
                            log.setDouble(2, amount);
                            log.setString(3, ledgerDescription);
                            log.executeUpdate();
                        }

                        c.commit();
                        UIUtils.info(this, "Successfully loaded PHP " + amount + " to your prepaid wallet via " + methodStr + "!");
                        loadData(); 
                        
                    } catch (Exception ex) {
                        c.rollback(); 
                        throw ex;
                    }
                }
            } catch (NumberFormatException ex) {
                UIUtils.error(this, new IllegalArgumentException("Please enter a valid numeric amount."));
            } catch (Exception ex) {
                UIUtils.error(this, ex);
            }
        }
    }
}

// =========================================================================
// Embedded UserSubscriptionPanel (Customer Side)
// =========================================================================
class UserSubscriptionPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);

    public UserSubscriptionPanel(UserSession session) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.session = session;

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("My Meal Subscriptions"), BorderLayout.WEST);

        // Buttons
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        JButton btnBuy = new JButton("Avail / Buy Meal Plan");
        btnBuy.setBackground(new Color(40, 167, 69)); // Green for purchasing
        btnBuy.setForeground(Color.WHITE);
        btnBuy.setFocusPainted(false);
        btnBuy.setFont(new Font("Tahoma", Font.BOLD, 12));

        JButton btnRefresh = new JButton("Refresh Status");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 12));

        rightButtons.add(btnBuy);
        rightButtons.add(btnRefresh);
        headerPanel.add(rightButtons, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- TABLE ---
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- LISTENERS ---
        btnRefresh.addActionListener(e -> loadData());
        btnBuy.addActionListener(e -> showPurchaseDialog());

        loadData();
    }

    // Fetches the live data so Admin updates reflect immediately!
    private void loadData() {
        try {
            String sql = "SELECT sub_id AS 'Sub ID', plan_id AS 'Plan ID', start_date AS 'Start Date', end_date AS 'Expiry Date', credits_total AS 'Total Credits', credits_used AS 'Used Credits', (credits_total - credits_used) AS 'Credits Remaining', status AS 'Status' " +
                         "FROM customer_subscriptions " +
                         "WHERE customer_id = (SELECT customer_id FROM customer_profiles WHERE user_id = ?) " +
                         "ORDER BY sub_id DESC";
            model.load(sql, session.userId);
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    // --- NEW: Purchase Logic ---
    private void showPurchaseDialog() {
        JComboBox<String> planCombo = new JComboBox<>(new String[]{
            "1 - Basic Plan (15 Meals) - PHP 1,000",
            "2 - Standard Plan (30 Meals) - PHP 1,900",
            "3 - Premium Semester Plan (60 Meals) - PHP 3,600"
        });
        
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{
            "Canteen Prepaid Wallet", "GCash / Maya", "Bank Transfer"
        });
        
        JTextField phoneField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField refField = new JTextField(15);
        
        // Disable external tracking fields by default
        phoneField.setEnabled(false);
        nameField.setEnabled(false);
        refField.setEnabled(false);

        // Dynamic logic: Unlocks exactly the fields the admin needs based on the payment type
        paymentCombo.addActionListener(e -> {
            int sel = paymentCombo.getSelectedIndex();
            phoneField.setEnabled(sel == 1); // Unlock for GCash
            nameField.setEnabled(sel == 2);  // Unlock for Bank Transfer
            refField.setEnabled(sel == 1 || sel == 2); // Unlock for both external methods
            
            // Auto-clear data if they switch methods
            if (sel == 0) { phoneField.setText(""); nameField.setText(""); refField.setText(""); }
        });

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Select Plan:")); panel.add(planCombo);
        panel.add(new JLabel("Payment Method:")); panel.add(paymentCombo);
        panel.add(new JLabel("Phone No. (GCash/Maya):")); panel.add(phoneField);
        panel.add(new JLabel("Account Name (Bank Transfer):")); panel.add(nameField);
        panel.add(new JLabel("Reference No.:")); panel.add(refField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Purchase Meal Subscription", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int sel = paymentCombo.getSelectedIndex();
                boolean isWallet = (sel == 0);
                boolean isGcash = (sel == 1);
                boolean isBank = (sel == 2);

                String phone = phoneField.getText().trim();
                String name = nameField.getText().trim();
                String ref = refField.getText().trim();

                // 1. STRICT VALIDATION
                if (isGcash) {
                    if (phone.isEmpty()) throw new IllegalArgumentException("Phone Number is required for GCash/Maya verification.");
                    if (ref.isEmpty()) throw new IllegalArgumentException("Reference Number is required.");
                } else if (isBank) {
                    if (name.isEmpty()) throw new IllegalArgumentException("Account Name is required for Bank Transfer verification.");
                    if (ref.isEmpty()) throw new IllegalArgumentException("Reference Number is required.");
                }

                String selectedPlan = (String) planCombo.getSelectedItem();
                int planId = Integer.parseInt(selectedPlan.split(" - ")[0]);
                
                int credits = 15; double cost = 1000.00; int validDays = 30;
                if (planId == 2) { credits = 30; cost = 1900.00; }
                else if (planId == 3) { credits = 60; cost = 3600.00; validDays = 120; }

                try (Connection c = DB.getConnection()) {
                    c.setAutoCommit(false);
                    try {
                        int cid = CanteenService.customerId(session.userId);

                        // 2. PROCESS WALLET IF SELECTED
                        if (isWallet) {
                            int wId = -1; double personalBal = 0, subsidyBal = 0;
                            try (PreparedStatement getW = c.prepareStatement("SELECT wallet_id, balance, subsidy_balance FROM wallets WHERE customer_id = ? FOR UPDATE")) {
                                getW.setInt(1, cid);
                                ResultSet rs = getW.executeQuery();
                                if (rs.next()) {
                                    wId = rs.getInt("wallet_id");
                                    personalBal = rs.getDouble("balance");
                                    subsidyBal = rs.getDouble("subsidy_balance");
                                } else throw new SQLException("Wallet account not activated.");
                            }

                            double availablePool = personalBal + subsidyBal;
                            if (availablePool < cost) throw new SQLException("Insufficient wallet funds. Combined Balance: PHP " + availablePool);

                            double deductSubsidy = Math.min(subsidyBal, cost);
                            double deductPersonal = cost - deductSubsidy;

                            try (PreparedStatement upd = c.prepareStatement("UPDATE wallets SET subsidy_balance = subsidy_balance - ?, balance = balance - ? WHERE wallet_id = ?")) {
                                upd.setDouble(1, deductSubsidy); 
                                upd.setDouble(2, deductPersonal); 
                                upd.setInt(3, wId); 
                                upd.executeUpdate();
                            }
                            
                            try (PreparedStatement log = c.prepareStatement("INSERT INTO wallet_transactions (wallet_id, amount, txn_type, description) VALUES (?, ?, 'DEDUCT', ?)")) {
                                log.setInt(1, wId); 
                                log.setDouble(2, cost); 
                                log.setString(3, "Purchased Meal Plan " + planId); 
                                log.executeUpdate();
                            }
                        }

                        // 3. PREPARE TRACKING DATA FOR ADMIN
                        String methodString = isWallet ? "WALLET" : (isGcash ? "GCASH" : "BANK_TRANSFER");
                        String refString = isWallet ? "AUTO-DEDUCT" : (isGcash ? "Phone: " + phone + " | Ref: " + ref : "Name: " + name + " | Ref: " + ref);
                        String subStatus = isWallet ? "ACTIVE" : "PENDING"; // PENDING for admin approval

                        // 4. INSERT SUBSCRIPTION WITH NEW COLUMNS
                        String subSql = "INSERT INTO customer_subscriptions (customer_id, plan_id, start_date, end_date, credits_total, credits_used, status, payment_method, payment_reference) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY), ?, 0, ?, ?, ?)";
                        try (PreparedStatement sub = c.prepareStatement(subSql)) {
                            sub.setInt(1, cid);
                            sub.setInt(2, planId);
                            sub.setInt(3, validDays);
                            sub.setInt(4, credits);
                            sub.setString(5, subStatus);
                            sub.setString(6, methodString);
                            sub.setString(7, refString);
                            sub.executeUpdate();
                        }

                        c.commit();
                        
                        // 5. ALERT THE USER BASED ON STATUS
                        if (isWallet) {
                            UIUtils.info(this, "Successfully availed Meal Plan! " + credits + " credits have been activated.");
                        } else {
                            UIUtils.info(this, "Request submitted! Your Meal Plan will activate once the Admin verifies your " + methodString + " payment.");
                        }
                        
                        loadData();

                    } catch (Exception ex) {
                        c.rollback(); throw ex;
                    }
                }
            } catch (IllegalArgumentException ex) { 
                UIUtils.error(this, ex); 
            } catch (Exception ex) { 
                UIUtils.error(this, ex); 
            }
        }
    }
}