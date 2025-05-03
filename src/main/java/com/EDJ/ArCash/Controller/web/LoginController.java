package com.EDJ.ArCash.Controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @PostMapping("/login")
    public String login(){
        return "login";
    }
}
