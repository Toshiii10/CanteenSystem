package com.csis;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.sql.*;

public class UserProfilePanel extends JPanel {
    private final UserSession s;
    private final JTextField name = new JTextField(20), email = new JTextField(20), phone = new JTextField(15), 
                             address = new JTextField(20), no = new JTextField(15), dept = new JTextField(20), 
                             diet = new JTextField(20), newpass = new JTextField(16);
                             
    private final JComboBox<String> profileSelect = new JComboBox<>(new String[]{
        "NONE", "DIABETIC_FRIENDLY", "LOW_SODIUM", "VEGETARIAN", "HALAL"
    });
    
    private final JComboBox<String> allergenCombo = new JComboBox<>(new String[]{
        "None", "Nuts", "Dairy", "Egg", "Seafood", "Gluten", "Soy"
    });

    // --- PICTURE COMPONENTS ---
    private final JLabel lblProfilePic = new JLabel("No Image");
    private byte[] profilePicBytes = null; // Stores image data

    public UserProfilePanel(UserSession s) {
        super(new GridBagLayout());
        this.s = s;
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        
        // --- 1. SETUP PROFILE PICTURE UI ---
        lblProfilePic.setPreferredSize(new Dimension(150, 150));
        lblProfilePic.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfilePic.setBorder(BorderFactory.createLineBorder(new Color(27, 54, 93), 2));

        // Upload Button
        JButton btnUpload = new JButton("Upload Picture");
        btnUpload.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnUpload.setBackground(Color.WHITE);
        btnUpload.setForeground(new Color(27, 54, 93));
        btnUpload.setFocusPainted(false);

        // Remove Button (Styled with a nice danger-red color to indicate removal)
        JButton btnRemove = new JButton("Remove Profile Picture");
        btnRemove.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRemove.setBackground(Color.WHITE);
        btnRemove.setForeground(new Color(220, 53, 69)); 
        btnRemove.setFocusPainted(false);

        // Upload Action
        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Profile Picture");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = fileChooser.getSelectedFile();
                    profilePicBytes = Files.readAllBytes(selectedFile.toPath());
                    
                    ImageIcon icon = new ImageIcon(new ImageIcon(profilePicBytes).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    lblProfilePic.setIcon(icon);
                    lblProfilePic.setText(""); 
                } catch (Exception ex) {
                    UIUtils.error(this, ex);
                }
            }
        });

        // Remove Action
        btnRemove.addActionListener(e -> {
            profilePicBytes = null; // Clear the memory
            lblProfilePic.setIcon(null); // Clear the UI image
            lblProfilePic.setText("No Image"); // Reset text
        });

        // THE FIX: Group both buttons side-by-side in a FlowLayout panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnUpload);
        buttonPanel.add(btnRemove);

        // Main Picture Container
        JPanel picContainer = new JPanel(new BorderLayout(5, 5));
        picContainer.setOpaque(false);
        picContainer.add(lblProfilePic, BorderLayout.CENTER);
        picContainer.add(buttonPanel, BorderLayout.SOUTH); // Put the side-by-side buttons at the bottom

        // Add Picture Container to the very top, spanning both columns
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; 
        add(picContainer, g);

        // --- 2. SETUP STANDARD FORM FIELDS ---
        g.gridwidth = 1; 

        String[] labels = {
            "Full Name", "Email", "Phone", "Address", "Student/Employee No.", 
            "Course/Department", "Additional Notes", "Dietary Restriction Profile", 
            "Food Allergy Restrictions", "New Password"
        };
        Component[] fs = {name, email, phone, address, no, dept, diet, profileSelect, allergenCombo, newpass};
        
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i + 1; 
            add(new JLabel(labels[i]), g);
            g.gridx = 1;
            add(fs[i], g);
        }
        
        JButton save = new JButton("Update Profile"), pass = new JButton("Change Password");
        g.gridx = 0; g.gridy = labels.length + 1; add(save, g);
        g.gridx = 1; add(pass, g);
        
        save.addActionListener(e -> save());
        pass.addActionListener(e -> password());
        load();
    }

    private void load() {
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement(
                "SELECT u.full_name,u.email,u.phone,u.address,c.student_employee_no,c.course_department,c.dietary_notes,c.dietary_profile,c.allergen_restrictions,c.profile_picture " +
                "FROM users u JOIN customer_profiles c ON c.user_id=u.user_id WHERE u.user_id=?")) {
            p.setInt(1, s.userId);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                name.setText(r.getString(1)); email.setText(r.getString(2));
                phone.setText(r.getString(3)); address.setText(r.getString(4));
                no.setText(r.getString(5)); dept.setText(r.getString(6));
                diet.setText(r.getString(7));
                
                String dietProf = r.getString(8);
                if (dietProf != null) profileSelect.setSelectedItem(dietProf);
                
                String allergy = r.getString(9);
                if (allergy != null && !allergy.trim().isEmpty()) {
                    String formattedAllergy = allergy.substring(0, 1).toUpperCase() + allergy.substring(1).toLowerCase();
                    allergenCombo.setSelectedItem(formattedAllergy);
                } else {
                    allergenCombo.setSelectedItem("None");
                }

                // Load Image Bytes into the UI
                profilePicBytes = r.getBytes(10);
                if (profilePicBytes != null && profilePicBytes.length > 0) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(profilePicBytes).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    lblProfilePic.setIcon(icon);
                    lblProfilePic.setText("");
                } else {
                    lblProfilePic.setIcon(null);
                    lblProfilePic.setText("No Image");
                }
            }
        } catch (Exception e) { UIUtils.error(this, e); }
    }

    private void save() {
        Connection c = null;
        try {
            c = DB.getConnection(); c.setAutoCommit(false);
            PreparedStatement u = c.prepareStatement("UPDATE users SET full_name=?,email=?,phone=?,address=? WHERE user_id=?");
            u.setString(1, name.getText()); u.setString(2, email.getText());
            u.setString(3, phone.getText()); u.setString(4, address.getText());
            u.setInt(5, s.userId); u.executeUpdate();
            
            PreparedStatement p = c.prepareStatement(
                "UPDATE customer_profiles SET student_employee_no=?,course_department=?,dietary_notes=?,dietary_profile=?,allergen_restrictions=?,profile_picture=? WHERE user_id=?");
            p.setString(1, no.getText()); p.setString(2, dept.getText()); p.setString(3, diet.getText());
            p.setString(4, String.valueOf(profileSelect.getSelectedItem()));
            
            String selectedAllergy = String.valueOf(allergenCombo.getSelectedItem());
            p.setString(5, "None".equals(selectedAllergy) ? "" : selectedAllergy.toLowerCase());
            
            // THE FIX: Safely handle SQL NULL if the user clicked "Remove"
            if (profilePicBytes == null || profilePicBytes.length == 0) {
                p.setNull(6, java.sql.Types.BLOB);
            } else {
                p.setBytes(6, profilePicBytes); 
            }
            
            p.setInt(7, s.userId); p.executeUpdate();
            
            c.commit();
            Audit.log(s, "UPDATE_PROFILE", "Updated profile nutrition preferences and picture");
            UIUtils.info(this, "Profile updated.");
        } catch (Exception e) {
            try { if (c != null) c.rollback(); } catch (Exception ignored) {}
            UIUtils.error(this, e);
        } finally {
            try { if (c != null) c.close(); } catch (Exception ignored) {}
        }
    }

    private void password() {
        if (newpass.getText().trim().length() < 6) { UIUtils.info(this, "Password must be at least 6 characters."); return; }
        try (Connection c = DB.getConnection(); PreparedStatement p = c.prepareStatement("UPDATE users SET password_hash=? WHERE user_id=?")) {
            p.setString(1, PasswordUtil.hash(newpass.getText())); p.setInt(2, s.userId); p.executeUpdate();
            newpass.setText(""); Audit.log(s, "CHANGE_PASSWORD", "Password changed"); UIUtils.info(this, "Password changed.");
        } catch (Exception e) { UIUtils.error(this, e); }
    }
}