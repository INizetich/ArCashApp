package com.EDJ.ArCash.Controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping({"/home", "/"})
    public String home(){
        return "demo";
    }

    @RequestMapping("/demo")
    public String demo(){
        return "demo";
    }

}
