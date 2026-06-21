package com.wip.assetmanagementsystem.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

   
    public static Map<String, Object> successResponse(
            String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }

   
    public static Map<String, Object> errorResponse(
            String message, int statusCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        response.put("statusCode", statusCode);
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }

   
    public static Map<String, Object> notFoundResponse(
            String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "not_found");
        response.put("message", message);
        response.put("statusCode", 404);
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }

  
    public static Map<String, Object> createdResponse(
            String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "created");
        response.put("message", message);
        response.put("data", data);
        response.put("statusCode", 201);
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }
}