package com.csis;

import java.nio.charset.StandardCharsets; 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil { 
    
    private PasswordUtil() {}

    public static String hash(String s) { 
        try { 
            MessageDigest d = MessageDigest.getInstance("SHA-256"); 
            byte[] b = d.digest(s.getBytes(StandardCharsets.UTF_8)); 
            
            StringBuilder x = new StringBuilder(2 * b.length); 
            for (byte v : b) {
                String hex = Integer.toHexString(0xff & v);
                if (hex.length() == 1) {
                    x.append('0');
                }
                x.append(hex);
            }
            return x.toString(); 
            
        } catch (NoSuchAlgorithmException e) { 
            throw new IllegalStateException("FATAL: SHA-256 algorithm missing from JVM", e); 
        }
    } 
}