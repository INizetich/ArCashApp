package com.EDJ.ArCash.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/home")
    public String home(){
        return "demo";
    }
}
