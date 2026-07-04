package com.csis;

public class UserSession { 
    public final int userId; 
    public final String username;
    public final String fullName;
    public final String role; 

    public UserSession(int userId, String username, String fullName, String role) { 
        this.userId = userId; 
        this.username = username; 
        this.fullName = fullName; 
        this.role = role; 
    } 
}