package com.example.ecommerceplatform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/public")
    public String publicGet()
    {
        return "user";
    }
}
