package com.EDJ.ArCash.Controller.api;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public String register(@RequestBody User user){
        userService.insertarUsuario(user);
        return "Usuario registrado con exito";
    }


}
