package com.csis;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterDialog extends JDialog {
    private final String[] labels = {
        "Username", "Password", "Full Name", "Email", "Phone", 
        "Address", "Student/Employee No.", "Course/Department", "Dietary Notes"
    };
    private final JTextField[] fields = new JTextField[labels.length];

    public RegisterDialog(Frame owner) {
        super(owner, "Register Customer Account", true);
        setSize(520, 490);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i;
            panel.add(new JLabel(labels[i] + ":"), g);
            
            g.gridx = 1;
            // Index 1 is the Password field
            fields[i] = (i == 1) ? new JPasswordField(22) : new JTextField(22);
            panel.add(fields[i], g);
        }
        
        JButton btnSave = new JButton("Create Account");
        JButton btnCancel = new JButton("Cancel");
        
        g.gridx = 0; g.gridy = labels.length;
        panel.add(btnSave, g);
        
        g.gridx = 1; 
        panel.add(btnCancel, g);
        
        add(panel);
        
        btnSave.addActionListener(e -> saveAccount());
        btnCancel.addActionListener(e -> dispose());
    }

    private void saveAccount() {
        if (fields[0].getText().trim().isEmpty() || fields[1].getText().isEmpty() || 
            fields[2].getText().trim().isEmpty() || fields[3].getText().trim().isEmpty()) {
            UIUtils.error(this, new IllegalArgumentException("Username, password, full name, and email are mandatory fields."));
            return;
        }

        String insertUserSql = "INSERT INTO users(username, password_hash, full_name, email, phone, address, role, status) VALUES(?, ?, ?, ?, ?, ?, 'CUSTOMER', 'ACTIVE')";
        String insertProfileSql = "INSERT INTO customer_profiles(user_id, student_employee_no, course_department, dietary_notes) VALUES(?, ?, ?, ?)";

        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false);
            
            try (PreparedStatement uStmt = c.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement pStmt = c.prepareStatement(insertProfileSql)) {
                 
                uStmt.setString(1, fields[0].getText().trim());
                uStmt.setString(2, PasswordUtil.hash(fields[1].getText()));
                uStmt.setString(3, fields[2].getText().trim());
                uStmt.setString(4, fields[3].getText().trim());
                uStmt.setString(5, fields[4].getText().trim());
                uStmt.setString(6, fields[5].getText().trim());
                uStmt.executeUpdate();
                
                int newUserId;
                try (ResultSet keys = uStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        newUserId = keys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve generated User ID.");
                    }
                }
                
                pStmt.setInt(1, newUserId);
                pStmt.setString(2, fields[6].getText().trim());
                pStmt.setString(3, fields[7].getText().trim());
                pStmt.setString(4, fields[8].getText().trim());
                pStmt.executeUpdate();
                
                c.commit();
                UIUtils.info(this, "Registration successful. You may now log in.");
                dispose();
                
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            }
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
}