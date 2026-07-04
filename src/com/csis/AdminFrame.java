package com.csis;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class AdminFrame extends JFrame { 
    private final UserSession s; 

    public AdminFrame(UserSession x) {
        super(AppConstants.APP_NAME + " - Admin / Staff Portal");
        s = x;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        // Header panel with dynamic welcome message and logout button, styled for a modern admin dashboard look.
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        };
        headerPanel.setBackground(new Color(245, 247, 250)); 
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(218, 222, 229)), 
            BorderFactory.createEmptyBorder(10, 20, 10, 20) 
        ));
        // Dynamic welcome message with admin's full name, styled for prominence and clarity.
        JLabel titleAndWelcomeLabel = new JLabel("Administrative Dashboard — Welcome, Admin (" + s.fullName + ")");
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
        // Logout action with confirmation dialog and session audit logging
        btnLogout.addActionListener(e -> {
            if (UIUtils.confirm(this, "De-authorize system manager session? Unsaved form fields will be dropped.")) {
                Audit.log(s, "LOGOUT", "System Manager logged out");
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
        buildTabs(t);
        
        add(t, BorderLayout.CENTER);
        applyGlobalTahomaTypography(this);
        
        this.revalidate();
        this.repaint();
    }
    // Recursively applies the Tahoma font to all components within the admin dashboard for a consistent and modern look.
    private void applyGlobalTahomaTypography(Component component) {
        if (component instanceof JTableHeader h) {
            Font f = h.getFont();
            h.setFont(new Font("Tahoma", f != null ? f.getStyle() : Font.BOLD, f != null ? f.getSize() : 13));
        } else {
            Font f = component.getFont();
            if (f != null) component.setFont(new Font("Tahoma", f.getStyle(), f.getSize()));
        }
        if (component instanceof Container c) {
            for (Component child : c.getComponents()) applyGlobalTahomaTypography(child);
        }
    }

    private CrudPanel crud(String title, String table, String id, String[] fields, String select, String search) {
        return new CrudPanel(new CrudConfig(title, table, id, fields, select, search), s);
    }
    // Builds the main tabbed interface for the admin dashboard, 
    // adding both custom panels and generic CRUD panels for various data entities.
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
            "SELECT e.employee_id, e.user_id, u.full_name AS 'Full Name', e.position, e.shift_schedule, e.hired_date, e.status " +
            "FROM employees e " +
            "JOIN users u ON e.user_id = u.user_id", "u.full_name"));
        
        t.addTab("Categories", crud("Menu Categories", "categories", "category_id", 
            new String[]{"category_name", "description", "status"}, 
            "SELECT category_id, category_name, description, status FROM categories", "category_name"));
        
        t.addTab("Menu Products", crud("Menu Products", "products", "product_id", 
            new String[]{"category_id", "product_name", "description", "barcode", "unit_price", "preparation_time_minutes", "status", "calories", "carbohydrates", "protein", "fat", "sugar", "sodium"}, 
            "SELECT product_id, category_id, product_name, description, barcode, unit_price, preparation_time_minutes, status, calories, carbohydrates, protein, fat, sugar, sodium FROM products", "product_name"));
        
        t.addTab("Ingredients", crud("Ingredients Inventory", "ingredients", "ingredient_id", 
            new String[]{"ingredient_name", "unit", "quantity_on_hand", "reorder_level", "status", "allergen_tags"}, 
            "SELECT ingredient_id, ingredient_name, unit, quantity_on_hand, reorder_level, status, allergen_tags FROM ingredients", "ingredient_name"));
        
        t.addTab("Recipes", new RecipeManagementPanel(s));
        
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
            "SELECT w.wallet_id, w.customer_id, u.full_name AS 'Account Name', w.balance, w.subsidy_balance, w.daily_limit, w.parent_sponsor_name, w.parent_contact " +
            "FROM wallets w " +
            "JOIN customer_profiles cp ON w.customer_id = cp.customer_id " +
            "JOIN users u ON cp.user_id = u.user_id", "u.full_name"));

        t.addTab("Allocate Subscriptions", new SubscriptionAllocationPanel(s));

        t.addTab("Promotions", crud("Promotions", "promotions", "promotion_id", 
            new String[]{"promo_name", "promo_code", "discount_type", "discount_value", "start_date", "end_date", "status"}, 
            "SELECT promotion_id, promo_name, promo_code, discount_type, discount_value, start_date, end_date, status FROM promotions", "promo_name"));
        
        t.addTab("Expenses", crud("Expenses", "expenses", "expense_id", 
            new String[]{"expense_date", "category", "description", "amount", "recorded_by"}, 
            "SELECT expense_id, expense_date, category, description, amount, recorded_by FROM expenses", "category"));
        
        t.addTab("Feedback", crud("Customer Feedback", "feedback", "feedback_id", 
            new String[]{"customer_id", "order_id", "rating", "comments", "status"}, 
            "SELECT feedback_id, customer_id, order_id, rating, comments, status, created_at FROM feedback", "comments"));
        
        t.addTab("Healthy Food Analytics", new HealthDemandsReportPanel());
        
        t.addTab("Kitchen Peak Performance", new UserSimplePanel("Peak-Hour Kitchen Waiting-Time Report", 
            "SELECT time_window, total_orders_processed AS 'Orders Packed', avg_lead_time_minutes AS 'Avg Total Window (m)', avg_actual_kitchen_cooking_minutes AS 'Avg Cooking Duration (m)' FROM v_peak_hour_analytics"));
        
        t.addTab("Monitor Unclaimed Orders", crud("Unclaimed Prepared Orders", "orders", "order_id", 
            new String[]{"order_status", "payment_status", "unclaimed_disposition", "notes"}, 
            "SELECT order_id, order_no, total_amount, payment_status, order_status, unclaimed_disposition, notes FROM orders WHERE order_status='READY'", "order_id"));

     t.addTab("Wallet Utilization Report", new UserSimplePanel("Prepaid Funds & Subsidy Auditing Ledger", 
            "SELECT cp.customer_id AS 'CUSTOMER ID', u.full_name AS 'FULL NAME', u.email AS 'EMAIL', " +
            "w.balance AS 'CURRENT BAL', w.subsidy_balance AS 'SUBSIDY BAL', w.daily_limit AS 'DAILY LIMIT', " +
            "w.parent_sponsor_name AS 'PARENT SPONSOR', w.parent_contact AS 'PARENT CONTACT', " +
            "COALESCE(SUM(CASE WHEN wt.txn_type = 'LOAD' THEN wt.amount ELSE 0 END), 0) AS 'TOTAL LOADED', " +
            "COALESCE(SUM(CASE WHEN wt.txn_type = 'DEDUCT' THEN wt.amount ELSE 0 END), 0) AS 'TOTAL SPENT', " +
            "COALESCE(SUM(CASE WHEN wt.txn_type = 'REFUND' THEN wt.amount ELSE 0 END), 0) AS 'TOTAL REFUNDED' " +
            "FROM wallets w " +
            "JOIN customer_profiles cp ON w.customer_id = cp.customer_id " +
            "JOIN users u ON cp.user_id = u.user_id " +
            "LEFT JOIN wallet_transactions wt ON w.wallet_id = wt.wallet_id " +
            "GROUP BY cp.customer_id, u.full_name, u.email, w.balance, w.subsidy_balance, w.daily_limit, w.parent_sponsor_name, w.parent_contact"
        ));
        
        t.addTab("Audit Logs", crud("Audit Logs", "audit_logs", "log_id", 
            new String[]{"user_id", "action", "details"}, 
            "SELECT log_id, user_id, action, details, created_at FROM audit_logs", "action"));

        t.addTab("Messages", new AdminMessagePanel(s));
        t.addTab("Reports", new ReportPanel());
    }
}

// =========================================================================
// Embedded RecipeManagementPanel (Sorted strictly by Recipe ID)
// =========================================================================
class RecipeManagementPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    private final JTextField recipeIdField = new JTextField(5);
    private final JComboBox<String> productCombo = new JComboBox<>();
    private final JComboBox<String> ingredientCombo = new JComboBox<>();
    private final JTextField qtyField = new JTextField(10);
    private final JTextField searchField = new JTextField(18);

    // This panel allows admins to manage recipes that link menu products to their required ingredients and quantities.
    public RecipeManagementPanel(UserSession session) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.session = session;

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("Recipe Management"), BorderLayout.WEST);

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchContainer.add(new JLabel("Search:"));
        searchContainer.add(searchField);
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");
        searchContainer.add(btnSearch); searchContainer.add(btnRefresh);
        headerPanel.add(searchContainer, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Recipe Details"));
        form.setPreferredSize(new Dimension(340, 0));
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        recipeIdField.setEditable(false);
        recipeIdField.setBackground(new Color(240, 240, 240));

        int r = 0;
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Recipe ID:"), g);
        g.gridx = 1; g.gridy = r++; form.add(recipeIdField, g);
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Target Product:"), g);
        g.gridx = 1; g.gridy = r++; form.add(productCombo, g);
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Ingredient:"), g);
        g.gridx = 1; g.gridy = r++; form.add(ingredientCombo, g);
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Qty Required:"), g);
        g.gridx = 1; g.gridy = r++; form.add(qtyField, g);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnClear = new JButton("Clear"); JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update Qty"); JButton btnDelete = new JButton("Delete");
        buttons.add(btnClear); buttons.add(btnAdd); buttons.add(btnUpdate); buttons.add(btnDelete);

        g.gridx = 0; g.gridy = r; g.gridwidth = 2; form.add(buttons, g);
        add(form, BorderLayout.EAST);

        loadDropdowns(); loadData();

        btnSearch.addActionListener(e -> doSearch());
        searchField.addActionListener(e -> doSearch());
        btnRefresh.addActionListener(e -> loadData());
        btnClear.addActionListener(e -> clearForm());
        btnAdd.addActionListener(e -> saveRecipe(false));
        btnUpdate.addActionListener(e -> saveRecipe(true));
        btnDelete.addActionListener(e -> deleteRecipe());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) populateFormFromTable();
        });
    }
    // Loads product and ingredient data into the respective dropdowns for recipe management.
    private void loadDropdowns() {
        try (Connection c = DB.getConnection(); Statement s = c.createStatement()) {
            ResultSet rsP = s.executeQuery("SELECT product_id, product_name FROM products ORDER BY product_name");
            while(rsP.next()) productCombo.addItem(rsP.getInt(1) + " - " + rsP.getString(2));
            ResultSet rsI = s.executeQuery("SELECT ingredient_id, ingredient_name, unit FROM ingredients ORDER BY ingredient_name");
            while(rsI.next()) ingredientCombo.addItem(rsI.getInt(1) + " - " + rsI.getString(2) + " (" + rsI.getString(3) + ")");
        } catch(Exception e) { e.printStackTrace(); }
    }
    // Loads recipe data into the table, joining with products and ingredients for display.
    private void loadData() {
        try {
            model.load("""
                SELECT r.recipe_id AS 'Recipe ID', p.product_name AS 'Menu Product', 
                       i.ingredient_name AS 'Ingredient', r.quantity_required AS 'Qty', 
                       i.unit AS 'Unit', i.allergen_tags AS 'Allergens'
                FROM recipes r JOIN products p ON r.product_id = p.product_id
                JOIN ingredients i ON r.ingredient_id = i.ingredient_id
                ORDER BY r.recipe_id ASC
                """);
        } catch (Exception e) { e.printStackTrace(); }
    }
    // Performs a search based on the input in the search field, filtering recipes by product or ingredient name.
    private void doSearch() {
        String term = "%" + searchField.getText().trim() + "%";
        try {
            model.load("""
                SELECT r.recipe_id AS 'Recipe ID', p.product_name AS 'Menu Product', 
                       i.ingredient_name AS 'Ingredient', r.quantity_required AS 'Qty', 
                       i.unit AS 'Unit', i.allergen_tags AS 'Allergens'
                FROM recipes r JOIN products p ON r.product_id = p.product_id
                JOIN ingredients i ON r.ingredient_id = i.ingredient_id
                WHERE p.product_name LIKE ? OR i.ingredient_name LIKE ?
                ORDER BY r.recipe_id ASC
                """, term, term);
        } catch (Exception e) { e.printStackTrace(); }
    }
    // Populates the form fields with data from the selected table row, allowing for easy updates or deletion of recipes.
    private void populateFormFromTable() {
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        
        recipeIdField.setText(String.valueOf(model.getValueAt(row, 0))); 
        String pName = String.valueOf(model.getValueAt(row, 1));
        String iName = String.valueOf(model.getValueAt(row, 2));
        qtyField.setText(String.valueOf(model.getValueAt(row, 3)));

        for (int i = 0; i < productCombo.getItemCount(); i++) {
            if (productCombo.getItemAt(i).contains(" - " + pName)) { productCombo.setSelectedIndex(i); break; }
        }
        for (int i = 0; i < ingredientCombo.getItemCount(); i++) {
            if (ingredientCombo.getItemAt(i).contains(" - " + iName + " ")) { ingredientCombo.setSelectedIndex(i); break; }
        }
    }
    // Clears the form fields and resets the selection, preparing the interface for a new entry or to clear out old data after an operation.
    private void clearForm() {
        recipeIdField.setText(""); qtyField.setText(""); table.clearSelection();
    }

    private void saveRecipe(boolean isUpdate) {
        if (productCombo.getSelectedItem() == null || ingredientCombo.getSelectedItem() == null || qtyField.getText().trim().isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Product, Ingredient, and Quantity are required.")); return;
        }
        if (isUpdate && recipeIdField.getText().isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Select a recipe from the table to update.")); return;
        }
        try {
            int pId = Integer.parseInt(String.valueOf(productCombo.getSelectedItem()).split(" - ")[0]);
            int iId = Integer.parseInt(String.valueOf(ingredientCombo.getSelectedItem()).split(" - ")[0]);
            double qty = Double.parseDouble(qtyField.getText().trim());

            String sql = isUpdate ? "UPDATE recipes SET product_id=?, ingredient_id=?, quantity_required=? WHERE recipe_id=?"
                                  : "INSERT INTO recipes (product_id, ingredient_id, quantity_required) VALUES (?, ?, ?)";
            try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
                p.setInt(1, pId); p.setInt(2, iId); p.setDouble(3, qty);
                if (isUpdate) p.setInt(4, Integer.parseInt(recipeIdField.getText().trim()));
                p.executeUpdate();
                Audit.log(session, isUpdate ? "UPDATE_RECIPE" : "ADD_RECIPE", "Product ID: " + pId + ", Ing ID: " + iId);
                loadData(); clearForm(); UIUtils.info(this, isUpdate ? "Recipe updated successfully." : "Ingredient added to recipe.");
            } catch (SQLIntegrityConstraintViolationException dup) {
                UIUtils.error(this, new IllegalStateException("This ingredient is already mapped to this product. Use 'Update' instead."));
            }
        } catch (NumberFormatException nfe) { UIUtils.error(this, new IllegalArgumentException("Quantity must be a valid number."));
        } catch (Exception e) { UIUtils.error(this, e); }
    }
    // Deletes the selected recipe after confirmation, removing the association between a product and an ingredient from the database.
    private void deleteRecipe() {
        if (recipeIdField.getText().isEmpty() || !UIUtils.confirm(this, "Remove this ingredient from the recipe?")) return;
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM recipes WHERE recipe_id=?")) {
            p.setInt(1, Integer.parseInt(recipeIdField.getText().trim())); p.executeUpdate();
            Audit.log(session, "DELETE_RECIPE", "Recipe ID: " + recipeIdField.getText());
            loadData(); clearForm();
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}
    class SubscriptionAllocationPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    
    private final JTextField subIdField = new JTextField(5);
    private final JComboBox<String> customerCombo = new JComboBox<>();
    private final JComboBox<String> planCombo = new JComboBox<>(new String[]{
        "1 - Basic Plan (15 Meals)", 
        "2 - Standard Plan (30 Meals)", 
        "3 - Premium Semester Plan (60 Meals)"
    });
    private final JTextField creditsField = new JTextField("30", 10);
    private final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"ACTIVE", "EXPIRED", "SUSPENDED"});
    private final JTextField searchField = new JTextField(18);

    public SubscriptionAllocationPanel(UserSession session) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.session = session;

        // Header & Search Bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("Manage Customer Meal Subscriptions"), BorderLayout.WEST);

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchContainer.add(new JLabel("Search Name:"));
        searchContainer.add(searchField);
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");
        searchContainer.add(btnSearch); searchContainer.add(btnRefresh);
        headerPanel.add(searchContainer, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Center Table
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Right-Side Form Panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Allocation Details"));
        form.setPreferredSize(new Dimension(360, 0));
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        subIdField.setEditable(false);
        subIdField.setBackground(new Color(240, 240, 240));

        int r = 0;
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Sub ID:"), g);
        g.gridx = 1; g.gridy = r++; form.add(subIdField, g);
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Select Customer:"), g);
        g.gridx = 1; g.gridy = r++; form.add(customerCombo, g);
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Meal Plan:"), g);
        g.gridx = 1; g.gridy = r++; form.add(planCombo, g);
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Total Credits:"), g);
        g.gridx = 1; g.gridy = r++; form.add(creditsField, g);
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Status:"), g);
        g.gridx = 1; g.gridy = r++; form.add(statusCombo, g);

        // Action Buttons
        JPanel buttons = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnClear = new JButton("Clear"); JButton btnAllocate = new JButton("Allocate Plan");
        JButton btnUpdate = new JButton("Update Status"); JButton btnDelete = new JButton("Delete");
        buttons.add(btnClear); buttons.add(btnAllocate); buttons.add(btnUpdate); buttons.add(btnDelete);

        g.gridx = 0; g.gridy = r; g.gridwidth = 2; form.add(buttons, g);
        add(form, BorderLayout.EAST);

        loadCustomers(); 
        loadData();

        // Listeners
        btnSearch.addActionListener(e -> doSearch());
        searchField.addActionListener(e -> doSearch());
        btnRefresh.addActionListener(e -> loadData());
        btnClear.addActionListener(e -> clearForm());
        btnAllocate.addActionListener(e -> saveSubscription(false));
        btnUpdate.addActionListener(e -> saveSubscription(true));
        btnDelete.addActionListener(e -> deleteSubscription());

        // Update total credits field automatically when a plan is selected
        planCombo.addActionListener(e -> {
            String selected = (String) planCombo.getSelectedItem();
            if (selected != null) {
                if (selected.contains("15")) creditsField.setText("15");
                else if (selected.contains("30")) creditsField.setText("30");
                else if (selected.contains("60")) creditsField.setText("60");
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) populateFormFromTable();
        });
    }

    // THE FIX: Automatically fetches real customer names from the database!
    private void loadCustomers() {
        try (Connection c = DB.getConnection(); Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery("SELECT cp.customer_id, u.full_name FROM customer_profiles cp JOIN users u ON cp.user_id = u.user_id WHERE u.status = 'ACTIVE' ORDER BY u.full_name");
            while(rs.next()) customerCombo.addItem(rs.getInt(1) + " - " + rs.getString(2));
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void loadData() {
        try {
            model.load("""
                SELECT cs.sub_id AS 'Sub ID', u.full_name AS 'Student Name', cs.plan_id AS 'Plan ID', 
                       cs.start_date AS 'Start', cs.end_date AS 'Expires', 
                       cs.credits_total AS 'Total Credits', cs.credits_used AS 'Used', cs.status AS 'Status'
                FROM customer_subscriptions cs 
                JOIN customer_profiles cp ON cs.customer_id = cp.customer_id
                JOIN users u ON cp.user_id = u.user_id
                ORDER BY cs.sub_id DESC
                """);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void doSearch() {
        String term = "%" + searchField.getText().trim() + "%";
        try {
            model.load("""
                SELECT cs.sub_id AS 'Sub ID', u.full_name AS 'Student Name', cs.plan_id AS 'Plan ID', 
                       cs.start_date AS 'Start', cs.end_date AS 'Expires', 
                       cs.credits_total AS 'Total Credits', cs.credits_used AS 'Used', cs.status AS 'Status'
                FROM customer_subscriptions cs 
                JOIN customer_profiles cp ON cs.customer_id = cp.customer_id
                JOIN users u ON cp.user_id = u.user_id
                WHERE u.full_name LIKE ?
                ORDER BY cs.sub_id DESC
                """, term);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void populateFormFromTable() {
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        subIdField.setText(String.valueOf(model.getValueAt(row, 0))); 
        String cName = String.valueOf(model.getValueAt(row, 1));
        statusCombo.setSelectedItem(String.valueOf(model.getValueAt(row, 7)));

        for (int i = 0; i < customerCombo.getItemCount(); i++) {
            if (customerCombo.getItemAt(i).contains(" - " + cName)) { customerCombo.setSelectedIndex(i); break; }
        }
    }

    private void clearForm() {
        subIdField.setText(""); table.clearSelection();
    }

    private void saveSubscription(boolean isUpdate) {
        if (customerCombo.getSelectedItem() == null || creditsField.getText().trim().isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Customer and Credits are required.")); return;
        }
        if (isUpdate && subIdField.getText().isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Select a subscription from the table to update.")); return;
        }
        try {
            // Parses the ID perfectly out of the smart dropdown selection
            int cId = Integer.parseInt(String.valueOf(customerCombo.getSelectedItem()).split(" - ")[0]);
            int pId = Integer.parseInt(String.valueOf(planCombo.getSelectedItem()).split(" - ")[0]);
            int credits = Integer.parseInt(creditsField.getText().trim());
            String status = String.valueOf(statusCombo.getSelectedItem());

            String sql = isUpdate ? "UPDATE customer_subscriptions SET status=? WHERE sub_id=?"
                                  : "INSERT INTO customer_subscriptions (customer_id, plan_id, start_date, end_date, credits_total, credits_used, status) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), ?, 0, ?)";
            
            try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
                if (isUpdate) {
                    p.setString(1, status);
                    p.setInt(2, Integer.parseInt(subIdField.getText().trim()));
                } else {
                    p.setInt(1, cId); p.setInt(2, pId); p.setInt(3, credits); p.setString(4, status);
                }
                p.executeUpdate();
                Audit.log(session, isUpdate ? "UPDATE_SUB" : "ALLOCATE_SUB", "Customer ID: " + cId);
                loadData(); clearForm(); UIUtils.info(this, isUpdate ? "Subscription updated." : "Meal Plan Allocated successfully!");
            }
        } catch (NumberFormatException nfe) { UIUtils.error(this, new IllegalArgumentException("Credits must be a valid number."));
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void deleteSubscription() {
        if (subIdField.getText().isEmpty() || !UIUtils.confirm(this, "Revoke and delete this meal subscription?")) return;
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM customer_subscriptions WHERE sub_id=?")) {
            p.setInt(1, Integer.parseInt(subIdField.getText().trim())); p.executeUpdate();
            Audit.log(session, "REVOKE_SUB", "Sub ID: " + subIdField.getText());
            loadData(); clearForm();
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}
// Advanced Health & Nutrition Demands Dashboard
class HealthDemandsReportPanel extends JPanel {
    public HealthDemandsReportPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTabbedPane healthTabs = new JTabbedPane();
        healthTabs.setFont(new Font("Tahoma", Font.BOLD, 12));

        // 1. Most Ordered Healthy Meals (Low Sugar < 8g, Low Sodium < 140mg)
        String healthyMealsSql = """
            SELECT p.product_name AS 'Healthy Meal', 
                   p.calories AS 'Calories', 
                   p.sugar AS 'Sugar (g)', 
                   p.sodium AS 'Sodium (mg)', 
                   COALESCE(SUM(oi.quantity), 0) AS 'Total Sold' 
            FROM products p 
            LEFT JOIN order_items oi ON p.product_id = oi.product_id 
            LEFT JOIN orders o ON oi.order_id = o.order_id AND o.order_status NOT IN ('CANCELLED', 'REJECTED') 
            WHERE p.sugar < 8.0 AND p.sodium < 140.0 
            GROUP BY p.product_id, p.product_name, p.calories, p.sugar, p.sodium 
            ORDER BY COALESCE(SUM(oi.quantity), 0) DESC
            """;
        healthTabs.addTab("Top Healthy Meals", new UserSimplePanel("Best-Selling Low Sugar & Low Sodium Meals", healthyMealsSql));

        // 2. Allergen-Sensitive Products Demand
        String allergenProductsSql = """
            SELECT p.product_name AS 'Menu Item', 
                   GROUP_CONCAT(DISTINCT i.allergen_tags SEPARATOR ', ') AS 'Contained Allergens', 
                   COALESCE(SUM(oi.quantity), 0) AS 'Times Ordered' 
            FROM products p 
            JOIN recipes r ON p.product_id = r.product_id 
            JOIN ingredients i ON r.ingredient_id = i.ingredient_id 
            LEFT JOIN order_items oi ON p.product_id = oi.product_id 
            WHERE i.allergen_tags IS NOT NULL AND i.allergen_tags != 'none' AND i.allergen_tags != '' 
            GROUP BY p.product_id, p.product_name 
            ORDER BY COALESCE(SUM(oi.quantity), 0) DESC
            """;
        healthTabs.addTab("Allergen Insights", new UserSimplePanel("Sales Tracker for Allergen-Heavy Products", allergenProductsSql));

        // 3. Nutrition-Based Menu Demand (Customer Demographics)
        String dietaryDemandSql = """
            SELECT cp.dietary_profile AS 'Dietary Profile / Goal', 
                   COALESCE(cp.allergen_restrictions, 'None') AS 'Reported Allergies', 
                   COUNT(cp.customer_id) AS 'Number of Students' 
            FROM customer_profiles cp 
            WHERE cp.dietary_profile != 'NONE' OR (cp.allergen_restrictions IS NOT NULL AND cp.allergen_restrictions != 'none') 
            GROUP BY cp.dietary_profile, cp.allergen_restrictions 
            ORDER BY COUNT(cp.customer_id) DESC
            """;
        healthTabs.addTab("Campus Diet Demand", new UserSimplePanel("Customer Dietary Profiles & Allergy Demographics", dietaryDemandSql));

        add(healthTabs, BorderLayout.CENTER);
            }
        }
