package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.DTO.LoginRequest;
import com.EDJ.ArCash.DTO.LoginResponse;
import com.EDJ.ArCash.DTO.UserResponse;
import com.EDJ.ArCash.Models.*;
import com.EDJ.ArCash.Repository.*;
import com.EDJ.ArCash.Security.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.EDJ.ArCash.Models.Imp.LogoutStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;

    @Autowired
    private RecoveryTokenService recoveryTokenService;

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<Credentials> credentialsOptional = credentialRepository.findByUsername(loginRequest.getUsername());

        if (credentialsOptional.isPresent()) {
            Credentials credentials = credentialsOptional.get();
            User usuario = credentials.getUser();

            if (!passwordEncoder.matches(loginRequest.getPassword(), credentials.getPass())) {
                return new LoginResponse(false, "Credenciales incorrectas", null, null, null,null);
            }

            if (!usuario.isActive()) {
                return new LoginResponse(false, "Usuario no habilitado", null, null, null,null);
            }

            List<RefreshToken> activeTokens = refreshTokenRepository.findAllByUserAndRevokedFalse(usuario);
            String refreshToken;
            if (!activeTokens.isEmpty()) {
                refreshToken = activeTokens.get(0).getRefreshToken();
            } else {
                refreshToken = JwtUtils.generateRefreshToken(String.valueOf(usuario.getId()), usuario.getPermissions().name());
                saveRefreshToken(usuario, refreshToken);
            }

            String accessToken = JwtUtils.generateToken(String.valueOf(usuario.getId()), usuario.getPermissions().name());

            Optional<Account> optionalAccount = accountRepository.findByUser_Id(usuario.getId());
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                return new LoginResponse(true, "Login exitoso", accessToken, refreshToken, account.getIdAccount(), usuario.getPermissions().name());
            }
            return new LoginResponse(false, "Cuenta no encontrada", null, null, null,null);
        }
        return new LoginResponse(false, "Usuario no encontrado", null, null, null,null);
    }

    public LogoutStatus logout(String accessToken) {
        try {
            Claims claims = JwtUtils.getClaimJWT(accessToken);
            String userId = claims.get("userID", String.class);

            if (userId == null) {
                return LogoutStatus.ERROR;
            }

            Long userIdLong = Long.parseLong(userId);

            Optional<User> userOptional = userRepository.findById(userIdLong);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                List<RefreshToken> activeTokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);

                if (activeTokens.isEmpty()) {
                    return LogoutStatus.ALREADY_REVOKED;
                }

                jwtUtils.revokeAllUserTokens(userIdLong);
                return LogoutStatus.SUCCESS;
            }

            return LogoutStatus.ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return LogoutStatus.ERROR;
        }
    }

    public boolean isValidSession(String token) {
        try {
            String userId = JwtUtils.extractUserId(token);
            if (userId == null) return false;

            return refreshTokenRepository.existsByUser_IdAndRevokedFalse(Long.parseLong(userId));

        } catch (Exception e) {
            return false;
        }
    }

    public void saveRefreshToken(User usuario, String refreshToken) {
        RefreshToken token = new RefreshToken();
        token.setUser(usuario);
        token.setRefreshToken(refreshToken);
        token.setIssuedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusDays(7));
        token.setRevoked(false);
        refreshTokenRepository.save(token);
    }


    @Transactional
    public boolean enviarCorreoRecuperacion(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            recoveryTokenRepository.deleteByUser_Id(user.getId());
            String token = recoveryTokenService.createRecoveryToken(user);

            try {
                emailService.testRecoverMail(user, token);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Transactional
    public String actualizarPassword(String tokenValue, String nuevaPassword, String confirmarPassword) {
        if (!nuevaPassword.equals(confirmarPassword)) {
            return "Las contraseñas no coinciden.";
        }

        Optional<RecoveryToken> recoveryToken = recoveryTokenRepository.findByToken(tokenValue);

        if (recoveryToken.isEmpty()) {
            return "Token no válido.";
        }

        RecoveryToken token = recoveryToken.get();

        if (token.isUsed()) {
            return "El token ya ha sido usado.";
        }

        if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
            return "El token ha expirado.";
        }

        User user = token.getUser();
        user.getCredentials().setPass(passwordEncoder.encode(nuevaPassword));
        userRepository.save(user);
        token.setUsed(true);
        recoveryTokenRepository.saveAndFlush(token);

        return "Contraseña actualizada correctamente.";
    }

    public boolean tokenValido(String tokenValue) {
        Optional<RecoveryToken> optionalToken = recoveryTokenRepository.findByToken(tokenValue);
        if (optionalToken.isEmpty()) return false;
        RecoveryToken token = optionalToken.get();
        return !token.isUsed() && token.getExpirationDate().isAfter(LocalDateTime.now());
    }



    @Transactional
    public boolean cambiarAliasYUsername(Long userId, String nuevoAlias) {
        String regex = "^(?=.*[A-Za-z])[A-Za-z\\d]{4,25}$";
        if (nuevoAlias == null || nuevoAlias.trim().isEmpty() ||
                !nuevoAlias.matches(regex) ||
                nuevoAlias.matches("^\\d+$")) { // solo números
            return false;
        }

        Optional<Account> accountOpt = accountRepository.findByUser_Id(userId);
        if (accountOpt.isEmpty()) return false;
        Credentials credentials = accountOpt.get().getUser().getCredentials();
        User user = accountOpt.get().getUser();


        if (credentialRepository.findByUsername(nuevoAlias).isPresent()) return false;

        user.setAlias(nuevoAlias);
        userRepository.saveAndFlush(user);

        credentials.setUsername(nuevoAlias);
        credentialRepository.save(credentials);
        return true;
    }





}