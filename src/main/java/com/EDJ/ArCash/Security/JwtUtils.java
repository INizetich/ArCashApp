package com.EDJ.ArCash.Security;

import com.EDJ.ArCash.DTO.AuthDTO.AccountResponse;
import com.EDJ.ArCash.Models.RefreshToken;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.RefreshTokenRepository;
import com.EDJ.ArCash.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.List;

@Component
public class JwtUtils {

    private static Key secretKey;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public JwtUtils(@Value("${spring.jwt.secret}") String signedJwt) {
        byte[] keyBytes = Base64.getDecoder().decode(signedJwt);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }



    public static String generateToken(String idUser, String role) {
        return Jwts.builder()
                .setSubject(idUser)
                .claim("userID",idUser)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(secretKey)
                .compact();
    }

    public static String generateRefreshToken(String idUser, String role) {
        return Jwts.builder()
                .setSubject(idUser)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 3600000)) // Expira en 7 días
                .signWith(secretKey)
                .compact();
    }

    public static Claims getClaimJWT(String token){
        JwtParserBuilder parserBuilder = Jwts.parserBuilder();
        return parserBuilder.setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }


    public ResponseEntity<?> validateAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(498).body(new AccountResponse(false, "Token no proporcionado", 0));
        }
        String token = authHeader.substring(7);

        // Verificar si el token es válido y no está revocado
        if (!isAccessTokenValid(token)) {
            return ResponseEntity.status(498).body(new AccountResponse(false, "Token inválido o revocado", 0));
        }

        Claims claims = getClaimJWT(token);
        String userIdStr = claims.get("userID", String.class);
        Long userId = userIdStr != null ? Long.parseLong(userIdStr) : null;

        if (userId == null) {
            return ResponseEntity.status(498).body(new AccountResponse(false, "Token inválido o sin userId", 0));
        }

        return ResponseEntity.status(200).body(userId);
    }




    public static String extractUserId(String token) {
        Claims claims = getClaimJWT(token);
        return claims.get("userID", String.class);
    }

    // Método para verificar si un token de acceso es válido y no está revocado
    public boolean isAccessTokenValid(String token) {
        try {
            Claims claims = getClaimJWT(token);
            String userId = claims.get("userID", String.class);
            Date expiration = claims.getExpiration();

            // Verificar si el usuario tiene algún refresh token activo
            Optional<RefreshToken> activeToken = refreshTokenRepository
                    .findByUserAndRevokedFalse(userRepository.findById(Long.parseLong(userId)).orElse(null));

            // Si no hay refresh token activo, el access token no debería ser válido
            if (activeToken.isEmpty()) {
                return false;
            }

            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Método para revocar todos los tokens de un usuario
    public void revokeAllUserTokens(Long userId) {
        Optional<User> optional = userRepository.findById(userId);
        if(optional.isPresent()){
            User user = optional.get();
            List<RefreshToken> validUserTokens = refreshTokenRepository
                    .findAllByUserAndRevokedFalse(user);

            if (!validUserTokens.isEmpty()) {
                validUserTokens.forEach(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
        }

        }else {
            System.out.println("Usuario no encontrado.");
        }
    }



}