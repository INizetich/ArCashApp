package com.EDJ.ArCash.Controller;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user){
        userService.insertarUsuario(user);
        return "register";
    }


    @PostMapping("/login")
    public String login(){
        return "login";
    }
}
