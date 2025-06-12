package com.EDJ.ArCash.Controller.api;


import com.EDJ.ArCash.DTO.AdminRequest;
import com.EDJ.ArCash.DTO.RegistrerRequest;
import com.EDJ.ArCash.DTO.UserResponse;
import com.EDJ.ArCash.Models.Imp.Permissions;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Service.AdminService;
import com.EDJ.ArCash.Service.AuthService;
import com.EDJ.ArCash.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class ApiAdminController {


    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    /// Lista de usuarios autenticados
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllAuthenticatedUsers(HttpServletRequest request) {
        adminService.validarAdmin(request);

        return ResponseEntity.ok(adminService.getAuthUsers());
    }


    @PutMapping("/users/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable Long id, HttpServletRequest request) {
        adminService.validarAdmin(request);
        adminService.disableUser(id);
        return ResponseEntity.ok("usuario deshabilitado correctamente");
    }

    @PutMapping("/users/{id}/enable")
    public ResponseEntity<?> enableUser(@PathVariable Long id, HttpServletRequest request) {
        adminService.validarAdmin(request);
        adminService.enableUser(id);
        return ResponseEntity.ok("usuario habilitado correctamente");
    }


    @PostMapping("/users/create-admin")
    public ResponseEntity<?> createAdminUser(@RequestBody AdminRequest adminRequest,HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        adminService.validarAdmin(request);
        User user = new User();
        user.setName(adminRequest.getName().substring(0,1).toUpperCase() + adminRequest.getName().substring(1).toLowerCase());
        user.setLastName(adminRequest.getLastName().substring(0,1).toUpperCase() + adminRequest.getLastName().substring(1).toLowerCase());
        user.setPermissions(Permissions.ADMIN);
        user.setDni(adminRequest.getDni());
        user.setEmail(adminRequest.getEmail());
        user.setAlias(adminRequest.getUsername());
        user.setEnabled(true);
        user.setActive(true);
        adminService.insertarAdministrador(user, adminRequest.getPassword());
        return ResponseEntity.ok("Usuario administrador creado correctamente");

    }

    @GetMapping("/check-access")
    public ResponseEntity<?> checkAdminAccess() {
        return ResponseEntity.ok().build();
    }

}




