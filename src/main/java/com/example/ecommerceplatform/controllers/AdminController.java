package com.example.ecommerceplatform.controllers;

import com.example.ecommerceplatform.models.UserEntity;
import com.example.ecommerceplatform.services.JpaUserDetailsService;
import com.example.ecommerceplatform.services.UserValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserValidatorService userValidatorService;
    private final JpaUserDetailsService userService;

    // Entry point to the main admin page
    @RequestMapping("/private")
    public String adminGet() {
        return "admin/admin";
    }

    // Method used to get the change password form
    @GetMapping("/private/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        System.out.println(model);
        return "admin/changePassword";
    }

    // Method used to change the password
    @PostMapping("/private/passwordChanged")
    public String passwordChanged(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult){
        userValidatorService.validate(userEntity, bindingResult);

        if (bindingResult.hasErrors())
            return "admin/changePassword";

        userService.save(userEntity);
        return "admin/admin";
    }
}
