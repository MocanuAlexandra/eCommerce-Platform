package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.models.ChangePasswordModel;
import com.tw.ecommerceplatform.services.JpaUserDetailsService;
import com.tw.ecommerceplatform.validators.ChangePasswordValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final ChangePasswordValidatorService changePasswordValidatorService;
    private final JpaUserDetailsService userService;

    // Entry point to the main admin page
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/private")
    public String open() {
        return "admin/admin";
    }

    // Method used to get the change password form
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("form", new ChangePasswordModel());
        return "admin/changePassword";
    }

    // Method used to actually change the password
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/changePassword")
    public String changePassword(@ModelAttribute("form") ChangePasswordModel form,
                                 Authentication authentication,
                                 BindingResult bindingResult) {

        // Validate the form
        changePasswordValidatorService.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/changePassword";
        }

        // Get the username from the authentication object and try to change the password
        try {
            String username = authentication.getName();
            userService.changePassword(username, form.getCurrentPassword(), form.getNewPassword());
        } catch (Exception e) {
            bindingResult.rejectValue("currentPassword", "error.form", "Incorrect current password");
            return "admin/changePassword";
        }

        // If the password was changed successfully, redirect to the admin page
        return "redirect:/private";
    }
}