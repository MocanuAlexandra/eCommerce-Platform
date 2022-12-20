package com.tw.ecommerceplatform.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShopController {

    // Endpoint to main page of shop' admin
    @PreAuthorize("hasRole('SHOP_ADMIN')")
    @RequestMapping("/shop")
    @ResponseBody
    public String getAdminShopPage() {
        return "Shop admin page";
    }

    // Endpoint to pending approval page
    @GetMapping("/register/shop/pending")
    public String pending() {
        return "register/registrationPending";
    }
}
