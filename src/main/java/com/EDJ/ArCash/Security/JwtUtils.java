package com.EDJ.ArCash.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtils {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("YV9zZWNyZXRfa2V5X3dpdGhfbm8tX3VuZGVyc2NvcmVz".getBytes(StandardCharsets.UTF_8));

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SECRET_KEY) // sin SignatureAlgorithm explícito, usa el del Key
                .compact();
    }
}
