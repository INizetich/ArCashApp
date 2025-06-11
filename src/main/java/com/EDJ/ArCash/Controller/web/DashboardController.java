package com.EDJ.ArCash.Controller.web;

import com.EDJ.ArCash.Security.JwtUtils;
import com.EDJ.ArCash.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DashboardController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtils jwtUtils;
    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request) {
     return "dashboard";
    }

    @GetMapping("/demo")
    public String dashboardnew(HttpServletRequest request) {
        return "dashboard-new";
    }
}
