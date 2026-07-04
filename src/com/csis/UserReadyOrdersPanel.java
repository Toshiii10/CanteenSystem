package com.csis;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class UserReadyOrdersPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    private final JLabel statusLabel = new JLabel(" ");
    private final Timer autoRefreshTimer;
    
    public UserReadyOrdersPanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("Ready for Pickup"), BorderLayout.WEST);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 13));
        
        // THE FIX: The new View Receipt button
        JButton btnViewReceipt = new JButton("View Receipt");
        btnViewReceipt.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnViewReceipt.setForeground(new Color(0, 102, 204));
        
        btnViewReceipt.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                UIUtils.info(this, "Please select an order from the table to view its receipt.");
                return;
            }
            int orderId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            new ReceiptDialog(this, orderId).setVisible(true);
        });

        // Grouping the buttons together on the right side
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightHeader.setBorder(BorderFactory.createEmptyBorder(7, 0, 0, 0));
        rightHeader.add(btnViewReceipt);
        rightHeader.add(btnRefresh);
        
        headerPanel.add(rightHeader, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        statusLabel.setForeground(new Color(40, 167, 69));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        add(statusLabel, BorderLayout.SOUTH);

        table.setFont(new Font("Tahoma", Font.PLAIN, 13)); 
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));

        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());
        loadData();

        autoRefreshTimer = new Timer(10_000, e -> loadData());
        autoRefreshTimer.start();

        addAncestorListener(new AncestorListener() {
            @Override public void ancestorRemoved(AncestorEvent event) { autoRefreshTimer.stop(); }
            @Override public void ancestorAdded(AncestorEvent event) { }
            @Override public void ancestorMoved(AncestorEvent event) { }
        });
    }

    private void loadData() {
        String sql = """
                SELECT o.order_id AS 'Order ID', o.order_no AS 'Order No', o.order_type AS 'Type', 
                       o.total_amount AS 'Total Amount', o.prep_ready_at AS 'Time Ready'
                FROM orders o
                JOIN customer_profiles c ON c.customer_id = o.customer_id
                WHERE c.user_id = ? AND o.order_status = 'READY'
                ORDER BY o.prep_ready_at ASC
                """;
        try {
            model.load(sql, session.userId);
            if (model.getRowCount() > 0) {
                statusLabel.setText("You have " + model.getRowCount() + " order(s) ready for pickup. Please proceed to the counter!");
            } else {
                statusLabel.setText("No orders are currently ready for pickup.");
            }
        } catch (Exception e) { 
            UIUtils.error(this, e); 
        }
    }
}

// =========================================================================
// NEW: Embedded ReceiptDialog Component
// =========================================================================
class ReceiptDialog extends JDialog {

    public ReceiptDialog(Component parent, int orderId) {
        super(SwingUtilities.getWindowAncestor(parent), "Official Receipt", Dialog.ModalityType.APPLICATION_MODAL);
        setSize(380, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JTextArea receiptArea = new JTextArea();
        receiptArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        receiptArea.setEditable(false);
        receiptArea.setBackground(new Color(255, 253, 245)); 
        receiptArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        receiptArea.setText(generateReceiptText(orderId));
        
        JScrollPane scroll = new JScrollPane(receiptArea);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        JButton btnClose = new JButton("Close Receipt");
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnClose.addActionListener(e -> dispose());
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnClose);
        add(bottom, BorderLayout.SOUTH);
    }

    private String generateReceiptText(int orderId) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String separator = "------------------------------------------\n";

        try (Connection c = DB.getConnection()) {
            String headerSql = "SELECT o.order_no, o.ordered_at, o.payment_method, " +
                               "o.subtotal, o.discount_amount, o.total_amount, o.amount_paid, u.full_name " +
                               "FROM orders o " +
                               "LEFT JOIN customer_profiles cp ON o.customer_id = cp.customer_id " +
                               "LEFT JOIN users u ON cp.user_id = u.user_id " +
                               "WHERE o.order_id = ?";
            
            try (PreparedStatement ph = c.prepareStatement(headerSql)) {
                ph.setInt(1, orderId);
                ResultSet rh = ph.executeQuery();
                
                if (rh.next()) {
                    sb.append("             UNIVERSITY CANTEEN             \n");
                    sb.append("          Official Digital Receipt          \n");
                    sb.append(separator);
                    sb.append(String.format("Order No : %s\n", rh.getString("order_no")));
                    sb.append(String.format("Date     : %s\n", rh.getTimestamp("ordered_at") != null ? sdf.format(rh.getTimestamp("ordered_at")) : "N/A"));
                    sb.append(String.format("Customer : %s\n", rh.getString("full_name") != null ? rh.getString("full_name") : "Walk-in"));
                    sb.append(String.format("Pay Mode : %s\n", rh.getString("payment_method")));
                    sb.append(separator);
                    sb.append(String.format("%-4s %-20s %14s\n", "QTY", "ITEM", "AMOUNT"));
                    sb.append(separator);

                    String itemsSql = "SELECT p.product_name, oi.quantity, oi.line_total " +
                                      "FROM order_items oi JOIN products p ON oi.product_id = p.product_id " +
                                      "WHERE oi.order_id = ?";
                    try (PreparedStatement pi = c.prepareStatement(itemsSql)) {
                        pi.setInt(1, orderId);
                        ResultSet ri = pi.executeQuery();
                        while (ri.next()) {
                            String itemName = ri.getString("product_name");
                            if (itemName.length() > 20) itemName = itemName.substring(0, 17) + "..."; 
                            
                            sb.append(String.format("%-4d %-20s PHP %10.2f\n", 
                                    ri.getInt("quantity"), 
                                    itemName, 
                                    ri.getDouble("line_total")));
                        }
                    }

                    sb.append(separator);
                    sb.append(String.format("%-25s PHP %10.2f\n", "SUBTOTAL:", rh.getDouble("subtotal")));
                    
                    double discount = rh.getDouble("discount_amount");
                    if (discount > 0) {
                        sb.append(String.format("%-25s PHP %10.2f\n", "DISCOUNT:", -discount));
                    }
                    
                    sb.append(String.format("%-25s PHP %10.2f\n", "TOTAL DUE:", rh.getDouble("total_amount")));
                    sb.append(separator);
                    sb.append(String.format("%-25s PHP %10.2f\n", "AMOUNT PAID:", rh.getDouble("amount_paid")));
                    
                    sb.append("\n");
                    sb.append("        Thank you for your order!         \n");
                    sb.append("      Please present this at the counter  \n");
                } else {
                    sb.append("Error: Order not found.");
                }
            }
        } catch (Exception e) {
            sb.append("Error generating receipt:\n").append(e.getMessage());
        }
        return sb.toString();
    }
}