package com.csis;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf; // Or FlatDarkLaf for dark mode

public class Main { 
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(new Runnable(){ 
            public void run(){ 
                try { 
                    // Initializes a modern, flat material UI theme globally
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    
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
}