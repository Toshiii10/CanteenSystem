package com.csis;

import javax.swing.*;
import java.awt.*;

public class PaymentReviewPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    private final JTextField searchField = new JTextField(18);
    private final JTextField paymentIdField = new JTextField(7);

    public PaymentReviewPanel(UserSession session) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.session = session;
        
        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("Payment Verification"), BorderLayout.WEST);

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchContainer.add(new JLabel("Search Ref/Order:")); searchContainer.add(searchField);
        JButton btnSearch = new JButton("Search"); JButton btnRefresh = new JButton("Refresh");
        searchContainer.add(btnSearch); searchContainer.add(btnRefresh);
        headerPanel.add(searchContainer, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // TABLE
        table.setFont(new Font("Tahoma", Font.PLAIN, 13)); table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // FORM
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Verify Payment"));
        form.setPreferredSize(new Dimension(320, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8); g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;

        paymentIdField.setEditable(false); paymentIdField.setBackground(new Color(240, 240, 240));
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Payment ID:"), g); g.gridx = 1; form.add(paymentIdField, g);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton btnVerify = new JButton("Verify"); JButton btnReject = new JButton("Reject");
        btnPanel.add(btnVerify); btnPanel.add(btnReject);
        g.gridx = 0; g.gridy = 1; g.gridwidth = 2; form.add(btnPanel, g);
        add(form, BorderLayout.EAST);
        
        btnSearch.addActionListener(e -> doSearch()); searchField.addActionListener(e -> doSearch());
        btnVerify.addActionListener(e -> processPayment(true)); btnReject.addActionListener(e -> processPayment(false));
        btnRefresh.addActionListener(e -> loadData());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) paymentIdField.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
        });
        loadData();
    }

    private void loadData() {
        try { model.load("SELECT p.payment_id, p.order_id, u.full_name AS customer, p.amount, p.method, p.reference_no, p.proof_reference, p.status, p.payment_date FROM payments p LEFT JOIN customer_profiles c ON c.customer_id = p.customer_id LEFT JOIN users u ON u.user_id = c.user_id ORDER BY p.payment_id DESC"); } catch (Exception e) { UIUtils.error(this, e); }
    }
    
    private void doSearch() {
        String term = "%" + searchField.getText().trim() + "%";
        try { model.load("SELECT p.payment_id, p.order_id, u.full_name AS customer, p.amount, p.method, p.reference_no, p.proof_reference, p.status, p.payment_date FROM payments p LEFT JOIN customer_profiles c ON c.customer_id = p.customer_id LEFT JOIN users u ON u.user_id = c.user_id WHERE p.reference_no LIKE ? OR CAST(p.order_id AS CHAR) LIKE ? OR u.full_name LIKE ? ORDER BY p.payment_id DESC", term, term, term); } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void processPayment(boolean isApproved) {
        String idText = paymentIdField.getText().trim();
        if (idText.isEmpty()) { UIUtils.error(this, new IllegalArgumentException("Specify a Payment ID.")); return; }
        try {
            int paymentId = Integer.parseInt(idText);
            CanteenService.verifyPayment(paymentId, session.userId, isApproved);
            Audit.log(session, isApproved ? "VERIFY_PAYMENT" : "REJECT_PAYMENT", "Processed Payment ID: " + paymentId);
            loadData(); paymentIdField.setText(""); table.clearSelection(); UIUtils.info(this, "Payment successfully updated.");
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}