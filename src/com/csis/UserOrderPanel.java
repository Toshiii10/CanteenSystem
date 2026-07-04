package com.csis;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

public class UserOrderPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel menuModel = new QueryTableModel();
    private final JTable menuTable = new JTable(menuModel);
    
    private final DefaultTableModel cartModel = new DefaultTableModel(
        new Object[]{"Product ID", "Product", "Qty", "Unit Price", "Subtotal", "Instruction"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int column) { return false; } 
    };
    private final JTable cartTable = new JTable(cartModel);
    
    private final JTextField quantityField = new JTextField("1", 3);
    private final JTextField notesField = new JTextField(12);
    private final JTextArea recommendationBox = new JTextArea(3, 50);
    
    private final JComboBox<String> paymentGateway = new JComboBox<>(new String[]{
        "CANTEEN PREPAID WALLET", "MEAL SUBSCRIPTION CREDIT", "MANUAL EXTERNAL GATEWAY"
    });
    private final DefaultComboBoxModel<String> slotComboModel = new DefaultComboBoxModel<>();
    private final JComboBox<String> slotComboBox = new JComboBox<>(slotComboModel);
    private final JComboBox<String> priorityBox = new JComboBox<>(new String[]{
        "REGULAR", "FACULTY", "PWD", "SENIOR"
    });

    public UserOrderPanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;
        
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5)); 
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topRow.add(UIUtils.title("Order Menu"));
        topRow.add(new JLabel("Qty:")); topRow.add(quantityField);
        topRow.add(new JLabel("Instruction:")); topRow.add(notesField);
        
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottomRow.add(new JLabel("Slot:")); bottomRow.add(slotComboBox);
        bottomRow.add(new JLabel("Type:")); bottomRow.add(priorityBox);
        bottomRow.add(new JLabel("Pay:")); bottomRow.add(paymentGateway);
        
        JButton btnAdd = new JButton("Add Selected");
        JButton btnRemove = new JButton("Remove Item");
        JButton btnCheckout = new JButton("Checkout");
        JButton btnRefresh = new JButton("Refresh Menu");
        
        bottomRow.add(btnAdd); bottomRow.add(btnRemove);
        bottomRow.add(btnCheckout); bottomRow.add(btnRefresh);
        
        headerPanel.add(topRow); headerPanel.add(bottomRow);
        add(headerPanel, BorderLayout.NORTH);
        
        recommendationBox.setEditable(false);
        recommendationBox.setLineWrap(true);
        recommendationBox.setWrapStyleWord(true);
        recommendationBox.setBorder(BorderFactory.createTitledBorder("Personalized Smart Menu Recommendations"));
        recommendationBox.setBackground(new Color(245, 250, 245));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(menuTable), new JScrollPane(cartTable));
        splitPane.setDividerLocation(220); 
        
        menuTable.setDefaultEditor(Object.class, null);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.add(recommendationBox, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
        
        btnAdd.addActionListener(e -> addToCart());
        btnRemove.addActionListener(e -> removeSelectedCartItem());
        btnCheckout.addActionListener(e -> processCheckout());
        btnRefresh.addActionListener(e -> { cartModel.setRowCount(0); loadData(); });
        
        menuTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && menuTable.getSelectedRow() != -1) {
                    quantityField.setText("1"); addToCart();
                }
            }
        });
        loadData();
    }
    
    private void loadData() {
        try {
            menuModel.load("SELECT product_id AS 'Product ID', product_name AS 'Item', unit_price AS 'Price', calories AS 'Calories', sugar AS 'Sugar (g)', sodium AS 'Sodium (mg)', status AS 'Status' FROM products WHERE status='AVAILABLE' ORDER BY product_name");
            loadActiveSlots();
            generateRecommendations();
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void loadActiveSlots() {
        slotComboModel.removeAllElements();
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); 
             ResultSet rs = st.executeQuery("SELECT slot_id, slot_time FROM pickup_slots WHERE status='ACTIVE' ORDER BY slot_time")) {
            while (rs.next()) slotComboModel.addElement(rs.getInt("slot_id") + " - " + rs.getTime("slot_time").toString());
        } catch (Exception ignored) {}
    }

    private void generateRecommendations() {
        try (Connection c = DB.getConnection()) {
            int customerId = CanteenService.customerId(session.userId);
            try (PreparedStatement p = c.prepareStatement("SELECT dietary_profile, allergen_restrictions FROM customer_profiles WHERE customer_id=?")) {
                p.setInt(1, customerId);
                try (ResultSet r = p.executeQuery()) {
                    if (r.next()) {
                        String profile = r.getString("dietary_profile");
                        StringBuilder query = new StringBuilder("SELECT product_name FROM products WHERE status='AVAILABLE' ");
                        if ("DIABETIC_FRIENDLY".equals(profile)) query.append("AND sugar < 6.00 ");
                        else if ("LOW_SODIUM".equals(profile)) query.append("AND sodium < 140.00 ");
                        else if ("VEGETARIAN".equals(profile)) query.append("AND (description LIKE '%vegetarian%' OR product_name LIKE '%cheese%') ");
                        else if ("HALAL".equals(profile)) query.append("AND product_name NOT LIKE '%pork%' AND product_name NOT LIKE '%adobo%' ");
                        query.append("LIMIT 4");
                        try (Statement st = c.createStatement(); ResultSet matches = st.executeQuery(query.toString())) {
                            StringBuilder recText = new StringBuilder("Menu Items matching your profile [" + profile + "]: ");
                            while (matches.next()) recText.append("\"").append(matches.getString(1)).append("\"   ");
                            recommendationBox.setText(recText.toString());
                        }
                    }
                }
            }
        } catch (Exception ex) { recommendationBox.setText("Set your health settings inside the Profile tab."); }
    }

    private void addToCart() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow < 0) {
            UIUtils.info(this, "Select an item from the menu list first.");
            return;
        }
        try {
            int quantityVal = Integer.parseInt(quantityField.getText().trim());
            if (quantityVal <= 0) throw new NumberFormatException();
            int modelRow = menuTable.convertRowIndexToModel(selectedRow);
            int targetId = ((Number) menuModel.getValueAt(modelRow, 0)).intValue();
            String pName = (String) menuModel.getValueAt(modelRow, 1);
            double cost = ((Number) menuModel.getValueAt(modelRow, 2)).doubleValue();
            
            int sugarCol = menuModel.findColumnByName("Sugar (g)");
            int sodiumCol = menuModel.findColumnByName("Sodium (mg)");
            double sugar = (sugarCol >= 0 && menuModel.getValueAt(modelRow, sugarCol) != null) ? ((Number) menuModel.getValueAt(modelRow, sugarCol)).doubleValue() : 0.0;
            double sodium = (sodiumCol >= 0 && menuModel.getValueAt(modelRow, sodiumCol) != null) ? ((Number) menuModel.getValueAt(modelRow, sodiumCol)).doubleValue() : 0.0;
            
            String note = notesField.getText().trim();

            try (Connection c = DB.getConnection()) {
                int cid = CanteenService.customerId(session.userId);
                PreparedStatement profileQuery = c.prepareStatement("SELECT dietary_profile, allergen_restrictions FROM customer_profiles WHERE customer_id=?");
                profileQuery.setInt(1, cid);
                ResultSet userProfile = profileQuery.executeQuery();
                
                if (userProfile.next()) {
                    String healthGoal = userProfile.getString("dietary_profile");
                    String healthAllergies = userProfile.getString("allergen_restrictions");
                    
                    if ("DIABETIC_FRIENDLY".equals(healthGoal) && sugar >= 8.00) {
                        if (!UIUtils.confirm(this, "⚠️ DIETARY CONFLICT: High sugar content (" + sugar + "g). Add anyway?")) return;
                    }
                    if ("LOW_SODIUM".equals(healthGoal) && sodium >= 140.00) {
                        if (!UIUtils.confirm(this, "⚠️ DIETARY CONFLICT: High sodium content (" + sodium + "mg). Add anyway?")) return;
                    }
                    
                    if (healthAllergies != null && !healthAllergies.trim().isEmpty() && !"none".equalsIgnoreCase(healthAllergies.trim())) {
                        PreparedStatement allergyCheck = c.prepareStatement(
                            "SELECT i.ingredient_name, i.allergen_tags FROM recipes r JOIN ingredients i ON r.ingredient_id = i.ingredient_id WHERE r.product_id = ? AND i.allergen_tags IS NOT NULL AND i.allergen_tags <> ''"
                        );
                        allergyCheck.setInt(1, targetId);
                        ResultSet componentAllergens = allergyCheck.executeQuery();
                        while (componentAllergens.next()) {
                            String ingredientName = componentAllergens.getString("ingredient_name");
                            String ingredientAllergens = componentAllergens.getString("allergen_tags").toLowerCase();
                            for (String restriction : healthAllergies.split(",")) {
                                String cleanRestriction = restriction.trim().toLowerCase();
                                if (!cleanRestriction.isEmpty() && ingredientAllergens.contains(cleanRestriction)) {
                                    if (!UIUtils.confirm(this, "🚨 ALLERGEN ALERT: [" + ingredientName.toUpperCase() + "] contains (" + ingredientAllergens + "). Add anyway?")) return;
                                }
                            }
                        }
                    }
                }
            }

            boolean itemExists = false;
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                if ((int) cartModel.getValueAt(i, 0) == targetId && cartModel.getValueAt(i, 5).equals(note)) {
                    int newQty = (int) cartModel.getValueAt(i, 2) + quantityVal;
                    cartModel.setValueAt(newQty, i, 2);
                    cartModel.setValueAt(newQty * cost, i, 4);
                    itemExists = true; break;
                }
            }
            if (!itemExists) cartModel.addRow(new Object[]{targetId, pName, quantityVal, cost, quantityVal * cost, note});
            quantityField.setText("1"); notesField.setText("");
        } catch (Exception e) { UIUtils.error(this, new IllegalArgumentException("Invalid Quantity.")); }
    }

    private void removeSelectedCartItem() {
        if (cartTable.getSelectedRow() < 0) return;
        cartModel.removeRow(cartTable.getSelectedRow());
    }

    private void processCheckout() {
        if (cartModel.getRowCount() == 0) { UIUtils.info(this, "Your cart is empty. Please select items from the menu before checking out."); return; }
        if (slotComboBox.getSelectedItem() == null) { UIUtils.info(this, "Please select a pickup time slot from the 'Slot' dropdown."); return; }

        String selectedSlotStr = String.valueOf(slotComboBox.getSelectedItem());
        int slotId = Integer.parseInt(selectedSlotStr.split(" - ")[0]);
        String priorityStr = String.valueOf(priorityBox.getSelectedItem());
        String method = String.valueOf(paymentGateway.getSelectedItem());

        try {
            int cid = CanteenService.customerId(session.userId);
            if (!CanteenService.checkSlotCapacity(slotId, new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()))) 
                throw new SQLException("Slot capacity reached.");

            int oid = CanteenService.createOrder(cid, "PICKUP", 0, "Scheduled pickup");
            try {
                try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE orders SET slot_id = ?, customer_type = ? WHERE order_id = ?")) {
                    ps.setInt(1, slotId); ps.setString(2, priorityStr); ps.setInt(3, oid); ps.executeUpdate();
                }
                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    CanteenService.addOrderItem(oid, (int)cartModel.getValueAt(i, 0), (int)cartModel.getValueAt(i, 2), (String)cartModel.getValueAt(i, 5));
                }
                
                if ("MEAL_SUBSCRIPTION_CREDIT".equals(method)) {
                    UIUtils.info(this, "Subscription checkout complete! ID: " + oid);
                } else if ("CANTEEN_PREPAID_WALLET".equals(method)) {
                    CanteenService.processWalletPayment(oid, cid);
                    UIUtils.info(this, "Order paid via Canteen Wallet! ID: " + oid);
                } else {
                    UIUtils.info(this, "Order submitted! ID: " + oid);
                }
                cartModel.setRowCount(0); loadData();
            } catch (Exception fatal) {
                CanteenService.setOrderStatus(oid, "CANCELLED"); throw fatal;
            }
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}