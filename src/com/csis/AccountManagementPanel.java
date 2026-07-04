package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

// The AccountManagementPanel class provides a user interface for administrators to manage user accounts in the system.
public class AccountManagementPanel extends JPanel {
    private final UserSession session;
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    
    private final JTextField selectedId = new JTextField(5);
    private final JTextField username = new JTextField(10);
    private final JTextField password = new JTextField(10);
    private final JTextField name = new JTextField(15);
    private final JTextField email = new JTextField(15);
    private final JComboBox<String> role = new JComboBox<>(new String[]{"ADMIN", "STAFF", "CUSTOMER"});

    public AccountManagementPanel(UserSession session) {
        super(new BorderLayout(8, 8));
        this.session = session;
        selectedId.setEditable(false);
        selectedId.setBackground(new Color(240, 240, 240));
        
        // RESPONSIVE UPGRADE: Split into stacking rows
        JPanel headerPanel = new JPanel(new BorderLayout(0, 5));
        headerPanel.add(UIUtils.title("Account Management"), BorderLayout.NORTH);
        
        // Row 1: Inputs
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        inputPanel.add(new JLabel("Selected ID:")); inputPanel.add(selectedId);
        inputPanel.add(new JLabel("Username:")); inputPanel.add(username);
        inputPanel.add(new JLabel("Password:")); inputPanel.add(password);
        inputPanel.add(new JLabel("Name:")); inputPanel.add(name);
        inputPanel.add(new JLabel("Email:")); inputPanel.add(email);
        inputPanel.add(new JLabel("Role:")); inputPanel.add(role);
        
        // Row 2: Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton btnCreate = new JButton("Create Account");
        JButton btnClear = new JButton("Clear Form");
        JButton btnReset = new JButton("Reset Password");
        JButton btnActive = new JButton("Activate");
        JButton btnInactive = new JButton("Deactivate");
        JButton btnRefresh = new JButton("Refresh");
        
        btnPanel.add(btnCreate); btnPanel.add(btnClear); btnPanel.add(btnReset); 
        btnPanel.add(btnActive); btnPanel.add(btnInactive); btnPanel.add(btnRefresh);
        
        headerPanel.add(inputPanel, BorderLayout.CENTER);
        headerPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        btnCreate.addActionListener(e -> createAccount());
        btnClear.addActionListener(e -> clearForm());
        btnReset.addActionListener(e -> resetPassword());
        btnActive.addActionListener(e -> updateStatus("ACTIVE"));
        btnInactive.addActionListener(e -> updateStatus("INACTIVE"));
        btnRefresh.addActionListener(e -> loadData());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int r = table.getSelectedRow();
                selectedId.setText(String.valueOf(table.getValueAt(r, 0)));
                username.setText(String.valueOf(table.getValueAt(r, 1)));
                name.setText(String.valueOf(table.getValueAt(r, 2)));
                email.setText(String.valueOf(table.getValueAt(r, 3)));
                role.setSelectedItem(String.valueOf(table.getValueAt(r, 4)));
                password.setText(""); 
            }
        });
        
        loadData();
    }

    private void loadData() {
        String sql = "SELECT user_id, username, full_name, email, role, status, created_at FROM users ORDER BY user_id";
        try {
            model.load(sql);
        } catch (Exception ex) { UIUtils.error(this, ex); }
    }
    
    private void clearForm() {
        selectedId.setText(""); username.setText(""); password.setText("");
        name.setText(""); email.setText(""); role.setSelectedIndex(0);
        table.clearSelection();
    }

    private void createAccount() {
        String inputUser = username.getText().trim(); String inputPass = password.getText();
        String inputName = name.getText().trim(); String inputEmail = email.getText().trim();

        if (inputUser.isEmpty() || inputPass.isEmpty() || inputName.isEmpty() || inputEmail.isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("All form fields are required to create a new account.")); return;
        }
        if (inputPass.length() < 6) {
            UIUtils.error(this, new IllegalArgumentException("Password must be at least six characters.")); return;
        }
        if (!inputEmail.contains("@")) {
            UIUtils.error(this, new IllegalArgumentException("Invalid email format detected.")); return;
        }

        String sql = "INSERT INTO users(username, password_hash, full_name, email, role, status) VALUES(?, ?, ?, ?, ?, 'ACTIVE')";
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, inputUser); p.setString(2, PasswordUtil.hash(inputPass));
            p.setString(3, inputName); p.setString(4, inputEmail);
            p.setString(5, String.valueOf(role.getSelectedItem()));
            p.executeUpdate();
            
            Audit.log(session, "CREATE_ACCOUNT", inputUser);
            loadData(); clearForm(); 
            UIUtils.info(this, "Account successfully created.");
        } catch (Exception ex) { UIUtils.error(this, ex); }
    }

    private void resetPassword() {
        String inputPass = password.getText();
        if (selectedId.getText().isEmpty()) { UIUtils.error(this, new IllegalArgumentException("Select a target account row first.")); return; }
        if (inputPass.length() < 6) { UIUtils.error(this, new IllegalArgumentException("Enter a new password of at least six characters.")); return; }

        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, PasswordUtil.hash(inputPass)); p.setInt(2, Integer.parseInt(selectedId.getText()));
            p.executeUpdate();
            Audit.log(session, "RESET_PASSWORD", selectedId.getText());
            password.setText(""); UIUtils.info(this, "Password reset successfully.");
        } catch (Exception ex) { UIUtils.error(this, ex); }
    }

    private void updateStatus(String newStatus) {
        if (selectedId.getText().isEmpty()) { UIUtils.error(this, new IllegalArgumentException("Select a target account row first.")); return; }
        
        int targetUserId = Integer.parseInt(selectedId.getText());
        if (targetUserId == session.userId && "INACTIVE".equals(newStatus)) {
            UIUtils.error(this, new IllegalArgumentException("Security Guard: You cannot deactivate your own signed-in account.")); return;
        }

        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, newStatus); p.setInt(2, targetUserId); p.executeUpdate();
            Audit.log(session, "ACCOUNT_" + newStatus, selectedId.getText());
            loadData();
        } catch (Exception ex) { UIUtils.error(this, ex); }
    }
}