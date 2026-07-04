package com.csis;

import javax.swing.*;
import java.awt.Font;
import com.formdev.flatlaf.FlatLightLaf; // Or FlatDarkLaf for dark mode

public class Main { 
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(new Runnable(){ 
            public void run(){ 
                try { 
                    // Initializes a modern, flat material UI theme globally
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    
                    // CORE UPGRADE: Force Tahoma globally AFTER setting the Look and Feel
                    setGlobalTahomaFont();
                    
                    // Optional UI Tweaks for cleaner round styles
                    UIManager.put("Button.arc", 12);
                    UIManager.put("Component.arc", 12);
                    UIManager.put("TextComponent.arc", 12);
                    UIManager.put("TabbedPane.showTabSeparators", true);
                } catch(Exception ignored){} 
                
                new AuthFrame().setVisible(true); 
            }
        }); 
    }

    // Universal Typography Injector
    public static void setGlobalTahomaFont() {
        Font tahomaFont = new Font("Tahoma", Font.PLAIN, 13);
        java.util.Enumeration<?> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, new javax.swing.plaf.FontUIResource(tahomaFont));
            }
        }
    }
}