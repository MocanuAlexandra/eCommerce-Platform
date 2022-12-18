package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.models.RegisterUserModel;
import com.tw.ecommerceplatform.models.RegisterWarehouseModel;
import com.tw.ecommerceplatform.repositories.RoleRepository;
import com.tw.ecommerceplatform.services.JpaUserDetailsService;
import com.tw.ecommerceplatform.validators.RegisterUserValidatorService;
import com.tw.ecommerceplatform.validators.RegisterWarehouseValidationService;
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
    private final RegisterUserValidatorService registerUserValidatorService;
    private final RegisterWarehouseValidationService registerWarehouseValidationService;
    private final RoleRepository roleRepository;

    @GetMapping("/register")
    public String open() {
        return "register/register";
    }

    @PostMapping("/register")
    public String register() {
        return "register/register";
    }


    // Register Customer
    @GetMapping("/register/customer")
    public String registerCustomer(Model model) {
        model.addAttribute("form", new RegisterUserModel());
        return "register/registerCustomer";
    }

    @PostMapping("/register/customer")
    public String registerCustomer(@ModelAttribute("form") RegisterUserModel form,
                           BindingResult bindingResult) {

        // Validate the form
        registerUserValidatorService.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "register/registerCustomer";
        }

        // Try to register the customer and redirect to the login page
        try {
            RoleEntity roleUser = roleRepository.findByName("ROLE_CUSTOMER");
            userService.registerCustomer(form.getUsername(), form.getPassword(),roleUser);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.user", "User already exists");
            return "register/registerCustomer";
        }
        return "redirect:/login";
    }


    // Register Warehouse Admin
    @GetMapping("/register/warehouse")
    public String registerWarehouseAdmin(Model model) {
        model.addAttribute("form", new RegisterWarehouseModel());
        return "register/registerWarehouse";
    }

    @PostMapping("/register/warehouse")
    public String registerWarehouseAdmin(@ModelAttribute("form") RegisterWarehouseModel form,
                           BindingResult bindingResult) {

        // Validate the form
        registerWarehouseValidationService.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "register/registerWarehouse";
        }
        return "redirect:/login";
    }
}
