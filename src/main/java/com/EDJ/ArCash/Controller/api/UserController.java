package com.EDJ.ArCash.Controller.api;

import com.EDJ.ArCash.DTO.RegisterResponse;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<RegisterResponse> register(@RequestBody User user) throws MessagingException, UnsupportedEncodingException {
        userService.insertarUsuario(user);
        return ResponseEntity.ok(new RegisterResponse(true,"Usuario registrado correctamente"));
    }


}
