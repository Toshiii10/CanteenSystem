package com.csis;

import java.io.*;
import java.sql.*;
import java.util.*;

public final class DB {
    private static final Properties P = new Properties();

    static {
        try {
            File f = new File("config/db.properties");
            if (f.exists()) {
                try (FileInputStream in = new FileInputStream(f)) {
                    P.load(in);
                }
            } else {
                // 1. Appended behavior rule to the initial default setup fallback
                P.setProperty("db.url", "jdbc:mysql://localhost:3306/canteen_sales_inventory_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Manila&zeroDateTimeBehavior=convertToNull");
                P.setProperty("db.user", "root");
                P.setProperty("db.password", "");
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.err.println("Database setup: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = P.getProperty("db.url");
        
        // 2. FAILSAFE INTERCEPTOR: If the external properties file lacks the parameter rule, append it automatically
        if (url != null && !url.contains("zeroDateTimeBehavior")) {
            if (url.contains("?")) {
                url += "&zeroDateTimeBehavior=convertToNull";
            } else {
                url += "?zeroDateTimeBehavior=convertToNull";
            }
        }
        
        return DriverManager.getConnection(url, P.getProperty("db.user"), P.getProperty("db.password", ""));
    }

    public static boolean test() {
        try (Connection c = getConnection()) {
            return c.isValid(3);
        } catch (Exception e) {
            return false;
        }
    }

    private DB() {}
}