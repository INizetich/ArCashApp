package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.DTO.AuthDTO.UserResponse;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.UserRepository;
import com.EDJ.ArCash.Security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Service
public class AdminService {


     @Autowired
     private UserRepository userRepository;

     @Autowired
     private CredentialsService credentialsService;

     @Autowired
     private AccountService accountService;

    // metodo para traer una lista de usuarios autenticados
    public List<UserResponse> getAuthUsers() {
        List<User> users = userRepository.findByEnabledTrue();
        return users.stream()
                .map(user -> {
                    Long idAccount = user.getAccounts() != null && !user.getAccounts().isEmpty()
                            ? user.getAccounts().get(0).getIdAccount()
                            : null;
                    String username = user.getAlias() != null ? user.getAlias() : null;
                    return new UserResponse(
                            user.getId(),
                            user.getName(),
                            user.getLastName(),
                            user.getDni(),
                            user.getEmail(),
                            username,
                            idAccount,
                            user.isEnabled(),
                            user.isActive()
                    );
                })
                .toList();
    }


    // metodo para hacer un soft delete a un usuario autenticado
    @Transactional
    public void disableUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        User user = userOpt.get();
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void enableUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        User user = userOpt.get();
        user.setActive(true);
        userRepository.save(user);
    }


    public void validarAdmin(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token no proporcionado");
        }
        String token = authHeader.substring(7);
        String role = JwtUtils.getClaimJWT(token).get("role", String.class);
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos de administrador");
        }
    }


    public void insertarAdministrador(User user, String rawPassword) {
        userRepository.save(user);
        credentialsService.createCredentials(user,rawPassword);
        accountService.createAccount(user,"PESOS");
    }


}
