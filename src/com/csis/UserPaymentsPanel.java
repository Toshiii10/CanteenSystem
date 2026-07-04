package com.csis;

import javax.swing.*;
import java.awt.*;

public class UserPaymentsPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    
    private final JTextField orderIdField = new JTextField(6);
    private final JTextField amountField = new JTextField(8);
    private final JTextField refField = new JTextField(12);
    private final JTextField proofField = new JTextField(16);
    private final JComboBox<String> methodCombo = new JComboBox<>(new String[]{"GCASH", "MAYA", "BANK_TRANSFER", "E_WALLET", "CASH"});

    public UserPaymentsPanel(UserSession session) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.session = session;
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(UIUtils.title("Submit Payment"), BorderLayout.WEST);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 13));
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        table.setFont(new Font("Tahoma", Font.PLAIN, 13)); table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Payment Details"));
        form.setPreferredSize(new Dimension(340, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8); g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;

        int r = 0;
        g.gridx = 0; g.gridy = r; form.add(new JLabel("Order ID:"), g); g.gridx = 1; form.add(orderIdField, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Amount:"), g); g.gridx = 1; form.add(amountField, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Method:"), g); g.gridx = 1; form.add(methodCombo, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Reference:"), g); g.gridx = 1; form.add(refField, g);
        g.gridx = 0; g.gridy = ++r; form.add(new JLabel("Proof/Link:"), g); g.gridx = 1; form.add(proofField, g);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton btnClear = new JButton("Clear"); JButton btnSubmit = new JButton("Submit");
        btnPanel.add(btnClear); btnPanel.add(btnSubmit);
        g.gridx = 0; g.gridy = ++r; g.gridwidth = 2; form.add(btnPanel, g);
        add(form, BorderLayout.EAST);
        
        btnSubmit.addActionListener(e -> submitPayment()); btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> loadData());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            // Extracts Order ID from the 2nd column (index 1)
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) orderIdField.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
        });
        loadData();
    }

    private void loadData() {
        try { model.load("SELECT p.payment_id AS 'Payment ID', p.order_id AS 'Order ID', p.payment_date AS 'Date', p.amount AS 'Amount', p.method AS 'Method', p.reference_no AS 'Reference', p.status AS 'Status', p.remarks AS 'Remarks' FROM payments p JOIN customer_profiles c ON c.customer_id = p.customer_id WHERE c.user_id = ? ORDER BY p.payment_id DESC", session.userId); } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void clearForm() { orderIdField.setText(""); amountField.setText(""); refField.setText(""); proofField.setText(""); table.clearSelection(); }

    private void submitPayment() {
        if (orderIdField.getText().trim().isEmpty() || amountField.getText().trim().isEmpty()) { UIUtils.error(this, new IllegalArgumentException("Order ID and Amount required.")); return; }
        try {
            int orderId = Integer.parseInt(orderIdField.getText().trim()); double amount = Double.parseDouble(amountField.getText().trim().replace(",", ""));
            CanteenService.submitPayment(orderId, CanteenService.customerId(session.userId), amount, String.valueOf(methodCombo.getSelectedItem()), refField.getText().trim(), proofField.getText().trim());
            Audit.log(session, "SUBMIT_PAYMENT", "Order ID: " + orderId);
            loadData(); clearForm(); UIUtils.info(this, "Payment submitted to administration for verification.");
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}