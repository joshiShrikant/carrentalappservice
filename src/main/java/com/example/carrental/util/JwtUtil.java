package com.example.carrental.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {


    private static SecretKey key;

    // Read the secret key from application.properties
    public JwtUtil(@Value("${jwt.secret}") String base64Secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }
//    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generate token
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000)) // 1 day
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate token and return claims
    public static Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract all claims
    public Claims extractAllClaims(String token) {
        return validateToken(token); // Reuse validation logic
    }

    // ---------------- Extract username ----------------
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ---------------- Extract roles ----------------
    public Object extractRoles(String token) {
        return extractAllClaims(token).get("roles");
    }

    // ---------------- Check expiration ----------------
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}