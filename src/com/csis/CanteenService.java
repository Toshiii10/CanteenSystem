package com.csis;
import java.sql.*; import java.text.SimpleDateFormat; import java.util.Date;

public final class CanteenService {

    private CanteenService() {}

    public static int customerId(int userId) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT customer_id FROM customer_profiles WHERE user_id=?")) {
            p.setInt(1, userId);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) return r.getInt(1);
                throw new SQLException("Customer profile not found.");
            }
        }
    }

    public static String newOrderNo() {
        return "ORD-" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    public static int createOrder(Integer customer, String type, int cashier, String notes) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement p = c.prepareStatement(
                     "INSERT INTO orders(order_no,customer_id,order_type,cashier_id,notes) VALUES(?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, newOrderNo());
            if (customer == null) p.setNull(2, Types.INTEGER); else p.setInt(2, customer);
            p.setString(3, type);
            if (cashier == 0) p.setNull(4, Types.INTEGER); else p.setInt(4, cashier);
            p.setString(5, notes);
            p.executeUpdate();
            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next()) return r.getInt(1);
                throw new SQLException("Failed to retrieve generated order ID.");
            }
        }
    }

    public static void addOrderItem(int orderId, int productId, int qty, String note) throws SQLException {
        String priceSql = "SELECT unit_price, status FROM products WHERE product_id = ?";

        // line_total is included explicitly to satisfy MySQL strict-mode NOT NULL checks
        // even though trg_order_item_total also computes it on insert.
        String insertSql = "INSERT INTO order_items(order_id, product_id, quantity, unit_price, line_total, special_instruction) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false);
            try {
                double price;
                try (PreparedStatement q = c.prepareStatement(priceSql)) {
                    q.setInt(1, productId);
                    try (ResultSet r = q.executeQuery()) {
                        if (!r.next() || !"AVAILABLE".equals(r.getString("status"))) {
                            throw new SQLException("Product is unavailable or does not exist.");
                        }
                        price = r.getDouble("unit_price");
                    }
                }

                double lineTotal = price * qty;
                try (PreparedStatement p = c.prepareStatement(insertSql)) {
                    p.setInt(1, orderId);
                    p.setInt(2, productId);
                    p.setInt(3, qty);
                    p.setDouble(4, price);
                    p.setDouble(5, lineTotal);
                    p.setString(6, note);
                    p.executeUpdate();
                }

                recalc(c, orderId);
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    private static void recalc(Connection c, int orderId) throws SQLException {
        String sql = """
            UPDATE orders o
            SET subtotal = (SELECT COALESCE(SUM(line_total),0) FROM order_items WHERE order_id=o.order_id),
                total_amount = (SELECT COALESCE(SUM(line_total),0) FROM order_items WHERE order_id=o.order_id) - discount_amount,
                balance = (SELECT COALESCE(SUM(line_total),0) FROM order_items WHERE order_id=o.order_id) - discount_amount - amount_paid
            WHERE order_id=?
            """;
        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, orderId);
            p.executeUpdate();
        }
    }

    public static void submitPayment(int orderId, int customer, double amount, String method, String ref, String proof) throws SQLException {
        try (Connection c = DB.getConnection()) {
            try (PreparedStatement v = c.prepareStatement(
                    "SELECT order_id FROM orders WHERE order_id=? AND customer_id=? AND order_status NOT IN ('CANCELLED','REJECTED') AND balance>0")) {
                v.setInt(1, orderId);
                v.setInt(2, customer);
                try (ResultSet vr = v.executeQuery()) {
                    if (!vr.next()) throw new SQLException("Order does not belong to your account or has no payable balance.");
                }
            }
            try (PreparedStatement p = c.prepareStatement(
                    "INSERT INTO payments(order_id,customer_id,amount,method,reference_no,proof_reference) VALUES(?,?,?,?,?,?)")) {
                p.setInt(1, orderId);
                p.setInt(2, customer);
                p.setDouble(3, amount);
                p.setString(4, method);
                p.setString(5, ref);
                p.setString(6, proof);
                p.executeUpdate();
            }
            try (PreparedStatement u = c.prepareStatement("UPDATE orders SET payment_status='PENDING' WHERE order_id=?")) {
                u.setInt(1, orderId);
                u.executeUpdate();
            }
        }
    }

    public static void verifyPayment(int paymentId, int userId, boolean accept) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement p = c.prepareStatement("UPDATE payments SET status=?,verified_by=?,verified_at=NOW() WHERE payment_id=? AND status='PENDING'")) {
            p.setString(1, accept ? "VERIFIED" : "REJECTED");
            p.setInt(2, userId);
            p.setInt(3, paymentId);
            if (p.executeUpdate() == 0) throw new SQLException("Payment already processed or not found.");
        }
    }

    public static void markPreparing(int orderId, int actor) throws SQLException {
        // BUGFIX: original used a bare (non-try-with-resources) Connection with manual
        // close() in a finally block, and created several PreparedStatements without
        // closing them at all (resource leak across every order moved to PREPARING).
        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false);
            try {
                String paymentStatus;
                boolean inventoryDeducted;
                try (PreparedStatement check = c.prepareStatement(
                        "SELECT payment_status,inventory_deducted FROM orders WHERE order_id=? FOR UPDATE")) {
                    check.setInt(1, orderId);
                    try (ResultSet r = check.executeQuery()) {
                        if (!r.next()) throw new SQLException("Order not found.");
                        paymentStatus = r.getString(1);
                        inventoryDeducted = r.getBoolean(2);
                    }
                }

                if (!"PAID".equals(paymentStatus)) {
                    throw new SQLException("Order must be fully paid before preparation.");
                }

                if (!inventoryDeducted) {
                    deductRecipeIngredients(c, orderId, actor);
                }

                try (PreparedStatement u = c.prepareStatement(
                        "UPDATE orders SET order_status='PREPARING',inventory_deducted=TRUE WHERE order_id=?")) {
                    u.setInt(1, orderId);
                    u.executeUpdate();
                }

                c.commit();
            } catch (Exception e) {
                c.rollback();
                if (e instanceof SQLException se) throw se;
                throw new SQLException(e);
            }
        }
    }

    /**
     * Deducts ingredient stock for every recipe component of the items on an order,
     * and logs each deduction as a stock movement. Extracted from markPreparing to keep
     * that method focused and to avoid leaking PreparedStatements/ResultSets created in a loop.
     */
    private static void deductRecipeIngredients(Connection c, int orderId, int actor) throws SQLException {
        record IngredientNeed(int ingredientId, double needed) {}
        java.util.List<IngredientNeed> needs = new java.util.ArrayList<>();

        try (PreparedStatement req = c.prepareStatement("""
                SELECT r.ingredient_id, SUM(r.quantity_required * oi.quantity) AS needed, i.quantity_on_hand
                FROM order_items oi
                JOIN recipes r ON r.product_id = oi.product_id
                JOIN ingredients i ON i.ingredient_id = r.ingredient_id
                WHERE oi.order_id = ?
                GROUP BY r.ingredient_id, i.quantity_on_hand
                FOR UPDATE
                """)) {
            req.setInt(1, orderId);
            try (ResultSet x = req.executeQuery()) {
                while (x.next()) {
                    double needed = x.getDouble("needed");
                    if (x.getDouble("quantity_on_hand") < needed) {
                        throw new SQLException("Insufficient ingredient stock. Ingredient ID: " + x.getInt(1));
                    }
                    needs.add(new IngredientNeed(x.getInt(1), needed));
                }
            }
        }

        try (PreparedStatement up = c.prepareStatement("UPDATE ingredients SET quantity_on_hand=quantity_on_hand-? WHERE ingredient_id=?");
             PreparedStatement mv = c.prepareStatement("""
                     INSERT INTO stock_movements(ingredient_id, movement_type, reference_table, reference_id, quantity_change, remarks, created_by)
                     VALUES(?,'SALE_USAGE','orders',?,?,'Recipe deduction',?)
                     """)) {
            for (IngredientNeed need : needs) {
                up.setDouble(1, need.needed());
                up.setInt(2, need.ingredientId());
                up.executeUpdate();

                mv.setInt(1, need.ingredientId());
                mv.setInt(2, orderId);
                mv.setDouble(3, -need.needed());
                mv.setInt(4, actor);
                mv.executeUpdate();
            }
        }
    }

    // State modifier with automatic wallet refund on cancellation/rejection of paid orders
    public static void setOrderStatus(int orderId, String status) throws SQLException {
        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false);
            try {
                int customerId = -1;
                double amount = 0;
                String paymentStatus = null;
                String oldOrderStatus = null;

                try (PreparedStatement ps = c.prepareStatement(
                        "SELECT customer_id, total_amount, payment_status, order_status FROM orders WHERE order_id = ?")) {
                    ps.setInt(1, orderId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            customerId = rs.getInt("customer_id");
                            amount = rs.getDouble("total_amount");
                            paymentStatus = rs.getString("payment_status");
                            oldOrderStatus = rs.getString("order_status");
                        } else {
                            throw new SQLException("Order not found.");
                        }
                    }
                }

                boolean isCancelOrReject = "CANCELLED".equals(status) || "REJECTED".equals(status);
                boolean wasAlreadyClosed = "CANCELLED".equals(oldOrderStatus) || "REJECTED".equals(oldOrderStatus);

                if (isCancelOrReject && "PAID".equals(paymentStatus) && !wasAlreadyClosed) {
                    refundToWallet(c, customerId, amount, orderId);
                }

                try (PreparedStatement update = c.prepareStatement("UPDATE orders SET order_status = ? WHERE order_id = ?")) {
                    update.setString(1, status);
                    update.setInt(2, orderId);
                    if (update.executeUpdate() == 0) {
                        throw new SQLException("Order not found.");
                    }
                }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException se) throw se;
                throw new SQLException(ex);
            }
        }
    }

    private static void refundToWallet(Connection c, int customerId, double amount, int orderId) throws SQLException {
        Integer walletId = null;
        try (PreparedStatement wCheck = c.prepareStatement("SELECT wallet_id FROM wallets WHERE customer_id = ?")) {
            wCheck.setInt(1, customerId);
            try (ResultSet wr = wCheck.executeQuery()) {
                if (wr.next()) walletId = wr.getInt("wallet_id");
            }
        }
        if (walletId == null) return;

        try (PreparedStatement refund = c.prepareStatement("UPDATE wallets SET balance = balance + ? WHERE wallet_id = ?")) {
            refund.setDouble(1, amount);
            refund.setInt(2, walletId);
            refund.executeUpdate();
        }
        try (PreparedStatement txn = c.prepareStatement(
                "INSERT INTO wallet_transactions (wallet_id, amount, txn_type, reference_order_id) VALUES (?, ?, 'REFUND', ?)")) {
            txn.setInt(1, walletId);
            txn.setDouble(2, amount);
            txn.setInt(3, orderId);
            txn.executeUpdate();
        }
    }

    // Checkout using prepaid wallet funds (personal balance + school subsidy)
    public static void processWalletPayment(int orderId, int customerId) throws SQLException {
        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false);
            try {
                double cost;
                try (PreparedStatement orderQuery = c.prepareStatement("SELECT total_amount FROM orders WHERE order_id = ?")) {
                    orderQuery.setInt(1, orderId);
                    try (ResultSet or = orderQuery.executeQuery()) {
                        if (!or.next()) throw new SQLException("Order not found.");
                        cost = or.getDouble("total_amount");
                    }
                }

                int walletId;
                double personalFunds, schoolSubsidy, dayLimit;
                try (PreparedStatement wQuery = c.prepareStatement(
                        "SELECT wallet_id, balance, subsidy_balance, daily_limit FROM wallets WHERE customer_id = ? FOR UPDATE")) {
                    wQuery.setInt(1, customerId);
                    try (ResultSet wr = wQuery.executeQuery()) {
                        if (!wr.next()) throw new SQLException("Canteen Wallet account record is missing.");
                        walletId = wr.getInt("wallet_id");
                        personalFunds = wr.getDouble("balance");
                        schoolSubsidy = wr.getDouble("subsidy_balance");
                        dayLimit = wr.getDouble("daily_limit");
                    }
                }

                if (dayLimit > 0.00) {
                    try (PreparedStatement spentToday = c.prepareStatement("""
                            SELECT COALESCE(SUM(amount), 0) FROM wallet_transactions
                            WHERE wallet_id = ? AND txn_type = 'DEDUCT' AND DATE(txn_date) = CURDATE()
                            """)) {
                        spentToday.setInt(1, walletId);
                        try (ResultSet sr = spentToday.executeQuery()) {
                            sr.next();
                            if (sr.getDouble(1) + cost > dayLimit) {
                                throw new SQLException("Transaction declined: Exceeds your configured daily spending limit of PHP " + dayLimit);
                            }
                        }
                    }
                }

                double availablePool = personalFunds + schoolSubsidy;
                if (availablePool < cost) throw new SQLException("Insufficient wallet funds. Combined Balance: PHP " + availablePool);

                double deductSubsidy = Math.min(schoolSubsidy, cost);
                double deductPersonal = cost - deductSubsidy;

                try (PreparedStatement deductWallet = c.prepareStatement(
                        "UPDATE wallets SET subsidy_balance = subsidy_balance - ?, balance = balance - ? WHERE wallet_id = ?")) {
                    deductWallet.setDouble(1, deductSubsidy);
                    deductWallet.setDouble(2, deductPersonal);
                    deductWallet.setInt(3, walletId);
                    deductWallet.executeUpdate();
                }

                try (PreparedStatement txn = c.prepareStatement(
                        "INSERT INTO wallet_transactions (wallet_id, amount, txn_type, reference_order_id) VALUES (?, ?, 'DEDUCT', ?)")) {
                    txn.setInt(1, walletId);
                    txn.setDouble(2, cost);
                    txn.setInt(3, orderId);
                    txn.executeUpdate();
                }

                try (PreparedStatement payOrder = c.prepareStatement(
                        "UPDATE orders SET amount_paid = total_amount, balance = 0, payment_status = 'PAID' WHERE order_id = ?")) {
                    payOrder.setInt(1, orderId);
                    payOrder.executeUpdate();
                }

                c.commit();
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException se) throw se;
                throw new SQLException(ex);
            }
        }
    }

    public static boolean checkSlotCapacity(int slotId, String dateStr) throws SQLException {
        try (Connection c = DB.getConnection()) {
            int booked;
            try (PreparedStatement p = c.prepareStatement(
                    "SELECT COUNT(*) FROM orders WHERE slot_id = ? AND DATE(ordered_at) = ? AND order_status <> 'CANCELLED'")) {
                p.setInt(1, slotId);
                p.setString(2, dateStr);
                try (ResultSet rs = p.executeQuery()) {
                    rs.next();
                    booked = rs.getInt(1);
                }
            }
            try (PreparedStatement cap = c.prepareStatement("SELECT max_order_capacity FROM pickup_slots WHERE slot_id = ?")) {
                cap.setInt(1, slotId);
                try (ResultSet capRs = cap.executeQuery()) {
                    if (capRs.next()) {
                        return booked < capRs.getInt("max_order_capacity");
                    }
                }
            }
        }
        return false;
    }

    public static void startKitchenPreparation(int orderId) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement p = c.prepareStatement("UPDATE orders SET order_status = 'PREPARING', prep_started_at = NOW() WHERE order_id = ?")) {
            p.setInt(1, orderId);
            if (p.executeUpdate() == 0) throw new SQLException("Order ID " + orderId + " not found.");
        }
    }

    public static void markKitchenReady(int orderId) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement p = c.prepareStatement("UPDATE orders SET order_status = 'READY', prep_ready_at = NOW() WHERE order_id = ?")) {
            p.setInt(1, orderId);
            if (p.executeUpdate() == 0) throw new SQLException("Order ID " + orderId + " not found.");
            sendSystemNotification(orderId, "Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.");
        }
    }

    public static void closeOrderServed(int orderId) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement p = c.prepareStatement("UPDATE orders SET order_status = 'COMPLETED', served_completed_at = NOW() WHERE order_id = ?")) {
            p.setInt(1, orderId);
            if (p.executeUpdate() == 0) throw new SQLException("Order ID " + orderId + " not found.");
        }
    }

    private static void sendSystemNotification(int orderId, String text) throws SQLException {
        String sql = """
                INSERT INTO notifications (user_id, title, body)
                SELECT cp.user_id, 'Order Ready Alert', ?
                FROM orders o JOIN customer_profiles cp ON o.customer_id = cp.customer_id
                WHERE o.order_id = ?
                """;
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, text);
            p.setInt(2, orderId);
            p.executeUpdate();
        }
    }
}