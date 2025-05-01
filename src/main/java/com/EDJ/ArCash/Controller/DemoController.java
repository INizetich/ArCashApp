package com.EDJ.ArCash.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import com.EDJ.ArCash.Models.User;

@Controller
public class DemoController {

    @RequestMapping({"/home", "/"})
    public String home(){
        return "demo";
    }

    @RequestMapping("/propuestas")
    public String propuestas(){
        return "logos";
    }


}
