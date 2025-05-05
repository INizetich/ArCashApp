package com.EDJ.ArCash.Controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String formulario(){
        return "registro";
    }


}
