package com.EDJ.ArCash.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Clave secreta (mínimo 32 caracteres alfanuméricos si se usa UTF-8)
    private static final String SECRET = "YV9zZWNyZXRfa2V5X3dpdGhfbm8tX3VuZGVyc2NvcmVz";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Eliminar el prefijo "Bearer "
            try {

                JwtParserBuilder parserBuilder = Jwts.parser();
                Claims claims = parserBuilder.setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();

                String username = claims.getSubject();

                if (username != null) {
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            username, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e) {
                logger.error("Token JWT inválido o expirado", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
