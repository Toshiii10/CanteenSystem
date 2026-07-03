package com.csis;

import javax.swing.*;
import java.awt.*;

public class PaymentReviewPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    private final JTextField paymentIdField = new JTextField(7);

    public PaymentReviewPanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;
        
        JPanel headerPanel = new JPanel();
        headerPanel.add(UIUtils.title("Payment Verification"));
        headerPanel.add(new JLabel("Payment ID:"));
        headerPanel.add(paymentIdField);
        
        JButton btnVerify = new JButton("Verify");
        JButton btnReject = new JButton("Reject");
        JButton btnRefresh = new JButton("Refresh");
        
        headerPanel.add(btnVerify);
        headerPanel.add(btnReject);
        headerPanel.add(btnRefresh);
        
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        btnVerify.addActionListener(e -> processPayment(true));
        btnReject.addActionListener(e -> processPayment(false));
        btnRefresh.addActionListener(e -> loadData());
        
        loadData();
    }

    private void loadData() {
        String sql = """
            SELECT p.payment_id, p.order_id, u.full_name AS customer, p.amount, 
                   p.method, p.reference_no, p.proof_reference, p.status, p.payment_date 
            FROM payments p 
            LEFT JOIN customer_profiles c ON c.customer_id = p.customer_id 
            LEFT JOIN users u ON u.user_id = c.user_id 
            ORDER BY p.payment_id DESC
            """;
        try {
            model.load(sql);
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }

    private void processPayment(boolean isApproved) {
        String idText = paymentIdField.getText().trim();
        if (idText.isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("You must specify a Payment ID to process."));
            return;
        }

        try {
            int paymentId = Integer.parseInt(idText);
            CanteenService.verifyPayment(paymentId, session.userId, isApproved);
            
            Audit.log(session, isApproved ? "VERIFY_PAYMENT" : "REJECT_PAYMENT", "Processed Payment ID: " + paymentId);
            loadData();
            paymentIdField.setText(""); // Clean runway
            UIUtils.info(this, "Payment successfully updated.");
            
        } catch (NumberFormatException nfe) {
            UIUtils.error(this, new IllegalArgumentException("Payment ID must be a valid number."));
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
}