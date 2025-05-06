package com.EDJ.ArCash.Controller.web;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
public class LoginController {



    @GetMapping("/PreLogin")
    public String loginPage() {
        return "login"; // nombre del HTML (login.html)
    }
}
