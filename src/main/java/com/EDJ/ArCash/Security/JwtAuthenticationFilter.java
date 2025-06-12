package com.EDJ.ArCash.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Key secretKey;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(@Value("${spring.jwt.secret}") String signedJwt, UserDetailsService userDetailsService) {
        byte[] keyBytes = Base64.getDecoder().decode(signedJwt);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
        String requestURI = request.getRequestURI();
        logger.info("Processing request to: {}", requestURI);

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                String role = claims.get("role", String.class);
                logger.info("User {} with role {} accessing {}", username, role, requestURI);

                // Verificación especial para rutas de admin
                if (requestURI.startsWith("/adminDashboard") && !"ADMIN".equals(role)) {
                    logger.warn("Non-admin user attempting to access admin dashboard");
                    response.sendRedirect("/dashboard");
                    return;
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Aquí se mapea el rol a ROLE_ADMIN o ROLE_USER
                    String springRole = "ROLE_" + role;
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(springRole))
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e) {
                logger.error("JWT validation failed: {}", e.getMessage());
                response.sendRedirect("/PreLogin");
                return;
            }
        } else if (requiresAuthentication(requestURI)) {
            logger.warn("Unauthenticated access attempt to protected resource");
            response.sendRedirect("/PreLogin");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean requiresAuthentication(String uri) {
        return !uri.equals("/") &&
                !uri.equals("/home") &&
                !uri.equals("/register") &&
                !uri.equals("/PreLogin") &&
                !uri.startsWith("/css/") &&
                !uri.startsWith("/js/") &&
                !uri.startsWith("/api/auth/") &&
                !uri.startsWith("/api/user/create") &&
                !uri.startsWith("/api/impuestos/") &&
                !uri.equals("/error") &&
                !uri.equals("/validate") &&
                !uri.equals("/forgot") &&
                !uri.equals("/reset-password") &&
                !uri.equals("/api/auth/send-recover-mail") &&
                !uri.equals("/validate-request") &&
                !uri.equals("/swagger-ui.html") &&
                !uri.startsWith("/swagger-ui/") &&
                !uri.startsWith("/v3/api-docs/") &&
                !uri.startsWith("/dashboard") &&
                !uri.startsWith("/adminDashboard");

    }
}