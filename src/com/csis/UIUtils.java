package com.csis;
import javax.swing.*; import java.awt.*;
import javax.swing.border.EmptyBorder;

public final class UIUtils { 
    
    // Generates a sleek modern corporate header
    public static JLabel title(String text) { 
        JLabel label = new JLabel(text); 
        label.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        label.setForeground(new Color(44, 62, 80)); // Deep Slate Blue Accent
        label.setBorder(new EmptyBorder(5, 5, 10, 5));
        return label; 
    } 
    
    // Generates a padded, modern styled interaction button
    public static JButton button(String text) { 
        JButton btn = new JButton(text); 
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn; 
    } 
    
    // Reusable stylized container frame panel with safe margin insets
    public static JPanel createCardPanel() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 233, 237), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        return card;
    }

    public static void info(Component c, String m) { 
        JOptionPane.showMessageDialog(c, m, AppConstants.APP_NAME, JOptionPane.INFORMATION_MESSAGE); 
    } 
    
    public static void error(Component c, Exception e) { 
        JOptionPane.showMessageDialog(c, e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE); 
    } 
    
    public static boolean confirm(Component c, String m) { 
        return JOptionPane.showConfirmDialog(c, m, "Confirm Action", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; 
    } 
    
    private UIUtils() {} 
}