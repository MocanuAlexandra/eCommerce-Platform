package com.tw.ecommerceplatform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLogin(@RequestParam(value = "error", required = false) String error, Model model) {

        if (error != null)
            addIntoModel(model, true, "Invalid credentials");
        else
            addIntoModel(model, false, "");
        return "login";
    }

    private void addIntoModel(Model model, boolean showError, String errorMessage) {
        model.addAttribute("showError", showError);
        model.addAttribute("errorMessage", errorMessage);
    }
}
