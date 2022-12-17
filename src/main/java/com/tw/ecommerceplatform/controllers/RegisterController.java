package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.models.RegisterUserModel;
import com.tw.ecommerceplatform.models.RoleEntity;
import com.tw.ecommerceplatform.repositories.RoleRepository;
import com.tw.ecommerceplatform.services.JpaUserDetailsService;
import com.tw.ecommerceplatform.validators.RegisterValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final JpaUserDetailsService userService;
    private final RegisterValidatorService registerValidatorService;
    private final RoleRepository roleRepository;

    @GetMapping("/register")
    public String open() {
        return "register/register";
    }

    @PostMapping("/register")
    public String register() {
        return "register/register";
    }

    @GetMapping("/register/customer")
    public String registerCustomer(Model model) {
        model.addAttribute("form", new RegisterUserModel());
        return "register/registerCustomer";
    }

    @PostMapping("/register/customer")
    public String register(@ModelAttribute("form") RegisterUserModel form,
                           BindingResult bindingResult) {

        // Validate the form
        registerValidatorService.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "register/registerCustomer";
        }

        // Try to register the user
        try {
            RoleEntity role_user = roleRepository.findByName("ROLE_CUSTOMER");
            userService.registerCustomer(form.getUsername(), form.getPassword(),role_user);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.user", "User already exists");
            return "register/registerCustomer";
        }
        return "redirect:/login";
    }
}
