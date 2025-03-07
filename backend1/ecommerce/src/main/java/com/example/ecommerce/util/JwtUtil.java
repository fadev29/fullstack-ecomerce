package com.example.ecommerce.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    public String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private  Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Menetapkan username sebagai subject
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Menetapkan waktu pembuatan
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // Token kedaluwarsa dalam 24 jam
                .signWith(SignatureAlgorithm.HS256, secretKey)  // Gunakan algoritma HS256 dan secret key
                .compact();
    }


    // validasi token dan user yang dikirim
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username1 = extractUsername(token);
        return username1.equals (userDetails.getUsername()) && !isTokenExpired(token);
    }
}