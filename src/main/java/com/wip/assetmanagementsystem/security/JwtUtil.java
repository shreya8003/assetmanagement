package com.wip.assetmanagementsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    
    private static final String SECRET = 
        "assetManagementSecretKey1234567890ABCDEFGH";

  
    private static final long EXPIRATION = 
        1000 * 60 * 60 * 10;

    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                    new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), 
                    SignatureAlgorithm.HS256)
                .compact();
    }

    
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

   
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

  
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    
    public boolean validateToken(
            String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) 
            && !isTokenExpired(token));
    }
}