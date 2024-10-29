package com.nit.security;


import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private String SECRET_KEY = "qwwerttyuiioplkjhgfdsazxcvbnm jdljdyucxmkjhsdykj";
    SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(Authentication auth) {
        String jwtString = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 846000000)) // Valid for 846000000 milliseconds (about 9.8 days)
                .claim("email", auth.getName())
                .signWith(key)
                .compact();
        return jwtString;
    }

    public String getEmailFromToken(String jwt) {
        jwt = jwt.substring(7); // Adjust for "Bearer " prefix
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        return String.valueOf(claims.get("email"));
    }
}
