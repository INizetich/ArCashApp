package com.EDJ.ArCash.Controller.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class AdminController {

    @GetMapping(value = {"/adminDashboard", "/adminDashboard/"})
    public String adminPanel(HttpServletRequest request){
        System.out.println("Accediendo a adminPanel desde: " + request.getRequestURI());
        return "admin";
    }
}
