package com.EDJ.ArCash.Controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {



    @PostMapping("/register")
    public String register(){
        return "register";
    }


    @PostMapping("/login")
    public String login(){
        return "login";
    }



}
