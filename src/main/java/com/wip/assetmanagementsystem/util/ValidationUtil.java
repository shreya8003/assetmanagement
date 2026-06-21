package com.wip.assetmanagementsystem.util;

public class ValidationUtil {

    
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

   
    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

   
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        String emailRegex = 
            "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return email.matches(emailRegex);
    }

   
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) return false;
        return phone.matches("\\d{10}");
    }

    
    public static boolean isValidAssetStatus(String status) {
        return status != null && (
            status.equalsIgnoreCase("active") ||
            status.equalsIgnoreCase("inactive") ||
            status.equalsIgnoreCase("under maintenance") ||
            status.equalsIgnoreCase("disposed")
        );
    }

    
    public static boolean isValidAssignmentStatus(String status) {
        return status != null && (
            status.equalsIgnoreCase("assigned") ||
            status.equalsIgnoreCase("returned") ||
            status.equalsIgnoreCase("pending")
        );
    }

   
    public static boolean isValidId(Integer id) {
        return id != null && id > 0;
    }
}