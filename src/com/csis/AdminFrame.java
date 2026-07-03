package com.csis;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AdminFrame extends JFrame { 
    private final UserSession s; 

    public AdminFrame(UserSession x) {
        super(AppConstants.APP_NAME + " - Admin / Staff Portal");
        s = x;
        
        // 1. Configure root window specifications and explicit BorderLayout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
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
        headerPanel.setBackground(new Color(245, 247, 250)); // Corporate slate premium off-white
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(218, 222, 229)), // Bottom divider line
            BorderFactory.createEmptyBorder(10, 20, 10, 20) // Natural horizontal and vertical padding
        ));

        // 3. Left-Aligned Text Content (Added directly to WEST position to force visibility)
        JLabel titleAndWelcomeLabel = new JLabel("Administrative Dashboard — Welcome, Admin (" + s.fullName + ")");
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
            boolean confirmClose = UIUtils.confirm(this, "De-authorize system manager session? Unsaved form fields will be dropped.");
            if (confirmClose) {
                Audit.log(s, "LOGOUT", "System Manager logged out cleanly from administrative root terminal");
                dispose(); 
                new AuthFrame().setVisible(true); 
            }
        });
        rightWrapper.add(btnLogout);
        headerPanel.add(rightWrapper, BorderLayout.EAST);
        
        // Anchor header panel securely across the northern segment
        add(headerPanel, BorderLayout.NORTH);
        
        // 5. Initialize and load all 27 navigation tabs
        JTabbedPane t = new JTabbedPane();
        t.setFont(new Font("Tahoma", Font.PLAIN, 13)); 
        t.setFocusable(false);
        buildTabs(t);
        
        add(t, BorderLayout.CENTER);
        
        // =====================================================================
        // CORE UPGRADE: Run the recursive global Tahoma typography injector pass
        // =====================================================================
        applyGlobalTahomaTypography(this);
        
        this.revalidate();
        this.repaint();
    }

    /**
     * Recursively traverses the admin terminal hierarchy and catches every table header,
     * cell column view layout, text entry box, and action button to apply Tahoma.
     */
    private void applyGlobalTahomaTypography(Component component) {
        // 1. Force styling on data grid table header row items explicitly
        if (component instanceof JTableHeader) {
            JTableHeader header = (JTableHeader) component;
            Font currentFont = header.getFont();
            int style = (currentFont != null) ? currentFont.getStyle() : Font.BOLD;
            int size = (currentFont != null) ? currentFont.getSize() : 13;
            header.setFont(new Font("Tahoma", style, size));
        } 
        // 2. Force styling on cells, input boxes, text areas, labels, and buttons
        else {
            Font currentFont = component.getFont();
            if (currentFont != null) {
                // Safely retain the layout's original size and structural weights (bold vs plain)
                component.setFont(new Font("Tahoma", currentFont.getStyle(), currentFont.getSize()));
            }
        }

        // 3. Drill down recursively into sub-containers
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyGlobalTahomaTypography(child);
            }
        }
    }

    private CrudPanel crud(String title, String table, String id, String[] fields, String select, String search) {
        return new CrudPanel(new CrudConfig(title, table, id, fields, select, search), s);
    }

    private void buildTabs(JTabbedPane t) {
        t.addTab("Dashboard", new DashboardPanel(s));
        t.addTab("POS", new PosPanel(s));
        t.addTab("Orders", new OrderProcessingPanel(s));
        t.addTab("Payments", new PaymentReviewPanel(s));
        t.addTab("Receive Stock", new ReceiveStockPanel(s));
        t.addTab("Accounts", new AccountManagementPanel(s));
        
        t.addTab("Customers", crud("Customer Profiles", "customer_profiles", "customer_id", 
            new String[]{"user_id", "student_employee_no", "course_department", "dietary_notes", "loyalty_points", "dietary_profile", "allergen_restrictions"}, 
            "SELECT customer_id, user_id, student_employee_no, course_department, dietary_notes, loyalty_points, dietary_profile, allergen_restrictions, created_at FROM customer_profiles", "student_employee_no"));
        
        t.addTab("Employees", crud("Employees", "employees", "employee_id", 
            new String[]{"user_id", "position", "shift_schedule", "hired_date", "status"}, 
            "SELECT employee_id, user_id, position, shift_schedule, hired_date, status FROM employees", "position"));
        
        t.addTab("Categories", crud("Menu Categories", "categories", "category_id", 
            new String[]{"category_name", "description", "status"}, 
            "SELECT category_id, category_name, description, status FROM categories", "category_name"));
        
        t.addTab("Menu Products", crud("Menu Products", "products", "product_id", 
            new String[]{"category_id", "product_name", "description", "barcode", "unit_price", "preparation_time_minutes", "image_reference", "status", "calories", "carbohydrates", "protein", "fat", "sugar", "sodium"}, 
            "SELECT product_id, category_id, product_name, description, barcode, unit_price, preparation_time_minutes, status, calories, carbohydrates, protein, fat, sugar, sodium FROM products", "product_name"));
        
        t.addTab("Ingredients", crud("Ingredients Inventory", "ingredients", "ingredient_id", 
            new String[]{"ingredient_name", "unit", "quantity_on_hand", "reorder_level", "status", "allergen_tags"}, 
            "SELECT ingredient_id, ingredient_name, unit, quantity_on_hand, reorder_level, status, allergen_tags FROM ingredients", "ingredient_name"));
        
        t.addTab("Recipes", crud("Recipe Ingredients", "recipes", "recipe_id", 
            new String[]{"product_id", "ingredient_id", "quantity_required"}, 
            "SELECT recipe_id, product_id, ingredient_id, quantity_required FROM recipes", "product_id"));
        
        t.addTab("Suppliers", crud("Suppliers", "suppliers", "supplier_id", 
            new String[]{"supplier_name", "contact_person", "phone", "email", "address", "status"}, 
            "SELECT supplier_id, supplier_name, contact_person, phone, email, address, status FROM suppliers", "supplier_name"));
        
        t.addTab("Batches", crud("Ingredient Batches", "ingredient_batches", "batch_id", 
            new String[]{"ingredient_id", "supplier_id", "batch_number", "received_date", "expiry_date", "quantity_received", "quantity_available", "unit_cost", "status"}, 
            "SELECT batch_id, ingredient_id, supplier_id, batch_number, received_date, expiry_date, quantity_received, quantity_available, unit_cost, status FROM ingredient_batches", "batch_number"));
        
        t.addTab("Purchases", crud("Purchase Orders", "purchase_orders", "purchase_id", 
            new String[]{"supplier_id", "order_date", "expected_date", "received_date", "total_amount", "status", "created_by", "notes"}, 
            "SELECT purchase_id, supplier_id, order_date, expected_date, received_date, total_amount, status, created_by, notes FROM purchase_orders", "status"));
        
        t.addTab("Purchase Items", crud("Purchase Items", "purchase_items", "purchase_item_id", 
            new String[]{"purchase_id", "ingredient_id", "quantity", "unit_cost", "line_total", "batch_number", "expiry_date"}, 
            "SELECT purchase_item_id, purchase_id, ingredient_id, quantity, unit_cost, line_total, batch_number, expiry_date FROM purchase_items", "purchase_id"));
        
        t.addTab("Order Items", crud("Order Items", "order_items", "order_item_id", 
            new String[]{"order_id", "product_id", "quantity", "unit_price", "line_total", "special_instruction"}, 
            "SELECT order_item_id, order_id, product_id, quantity, unit_price, line_total, special_instruction FROM order_items", "order_id"));
        
        t.addTab("Adjustments", crud("Inventory Adjustments", "inventory_adjustments", "adjustment_id", 
            new String[]{"ingredient_id", "batch_id", "adjustment_type", "quantity", "reason", "adjusted_by"}, 
            "SELECT adjustment_id, ingredient_id, batch_id, adjustment_date, adjustment_type, quantity, reason, adjusted_by FROM inventory_adjustments", "reason"));
        
        t.addTab("Movements", crud("Stock Movements", "stock_movements", "movement_id", 
            new String[]{"ingredient_id", "movement_type", "reference_table", "reference_id", "quantity_change", "remarks", "created_by"}, 
            "SELECT movement_id, ingredient_id, movement_type, reference_table, reference_id, quantity_change, movement_date, remarks, created_by FROM stock_movements", "movement_type"));
        
        t.addTab("Manage Customer Wallets", crud("Prepaid Wallets Ledger", "wallets", "wallet_id", 
            new String[]{"customer_id", "balance", "subsidy_balance", "daily_limit", "parent_sponsor_name", "parent_contact"}, 
            "SELECT wallet_id, customer_id, balance, subsidy_balance, daily_limit, parent_sponsor_name, parent_contact FROM wallets", "customer_id"));

        t.addTab("Allocate Subscriptions", crud("Active Customer Subscriptions", "customer_subscriptions", "sub_id", 
            new String[]{"customer_id", "plan_id", "start_date", "end_date", "credits_total", "credits_used", "status"}, 
            "SELECT sub_id, customer_id, plan_id, start_date, end_date, credits_total, credits_used, status FROM customer_subscriptions", "customer_id"));

        t.addTab("Promotions", crud("Promotions", "promotions", "promotion_id", 
            new String[]{"promo_name", "promo_code", "discount_type", "discount_value", "start_date", "end_date", "status"}, 
            "SELECT promotion_id, promo_name, promo_code, discount_type, discount_value, start_date, end_date, status FROM promotions", "promo_name"));
        
        t.addTab("Expenses", crud("Expenses", "expenses", "expense_id", 
            new String[]{"expense_date", "category", "description", "amount", "recorded_by"}, 
            "SELECT expense_id, expense_date, category, description, amount, recorded_by FROM expenses", "category"));
        
        t.addTab("Feedback", crud("Customer Feedback", "feedback", "feedback_id", 
            new String[]{"customer_id", "order_id", "rating", "comments", "status"}, 
            "SELECT feedback_id, customer_id, order_id, rating, comments, status, created_at FROM feedback", "comments"));
        
        t.addTab("Healthy Food Demand", new UserSimplePanel("Menu Health Statistics & Order Counts", 
            "SELECT * FROM v_nutrition_analytics ORDER BY total_orders DESC"));
        
        t.addTab("Kitchen Peak Performance", new UserSimplePanel("Peak-Hour Kitchen Waiting-Time Report", 
            "SELECT time_window, total_orders_processed AS 'Orders Packed', avg_lead_time_minutes AS 'Avg Total Window (m)', avg_actual_kitchen_cooking_minutes AS 'Avg Cooking Duration (m)' FROM v_peak_hour_analytics"));
        
        t.addTab("Monitor Unclaimed Orders", crud("Unclaimed Prepared Orders", "orders", "order_id", 
            new String[]{"order_status", "payment_status", "unclaimed_disposition", "notes"}, 
            "SELECT order_id, order_no, total_amount, payment_status, order_status, unclaimed_disposition, notes FROM orders WHERE order_status='READY'", "order_id"));

        t.addTab("Wallet Utilization Report", new UserSimplePanel("Prepaid Funds & Subsidy Auditing Ledger", 
            "SELECT * FROM v_wallet_utilization ORDER BY current_balance DESC"));
        
        t.addTab("Audit Logs", crud("Audit Logs", "audit_logs", "log_id", 
            new String[]{"user_id", "action", "details"}, 
            "SELECT log_id, user_id, action, details, created_at FROM audit_logs", "action"));
        
        t.addTab("Reports", new ReportPanel());
    }
}