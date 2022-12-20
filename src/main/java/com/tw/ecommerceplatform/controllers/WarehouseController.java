package com.tw.ecommerceplatform.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WarehouseController {

    // Endpoint to main page of warehouse' admin
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @RequestMapping("/warehouse")
    @ResponseBody
    public String getWarehouseAdminPage() {
        return "Warehouse admin page";
    }

    // Endpoint to pending approval page
    @GetMapping("/register/warehouse/pending")
    public String pending() {
        return "register/registrationPending";
    }
}
