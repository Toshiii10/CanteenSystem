package com.csis;

import java.sql.*;

public final class Audit { 
    
    private Audit() {} // Prevent instantiation

    public static void log(UserSession s, String action, String detail) { 
        if (s == null) return; 
        
        String sql = "INSERT INTO audit_logs(user_id, action, details) VALUES(?, ?, ?)";
        try (Connection c = DB.getConnection(); 
             PreparedStatement p = c.prepareStatement(sql)) {
             
            p.setInt(1, s.userId); 
            p.setString(2, action);
            p.setString(3, detail);
            p.executeUpdate();
            
        } catch (SQLException e) {
            // Never silently swallow exceptions. Log to stderr so DevOps can track DB drops.
            System.err.println("CRITICAL: Audit Log Insertion Failed - " + e.getMessage());
        } 
    } 
}