package com.tw.ecommerceplatform.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @RequestMapping("/public")
    public String publicGet() {
        return "user";
    }
}
