package com.tw.ecommerceplatform.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomerController {
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequestMapping("/customer")
    public String getCustomerPanelPage() {
        return "customer/customerPanel";
    }
}
