package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.models.RegisterUserModel;
import com.tw.ecommerceplatform.models.RegisterWarehouseModel;
import com.tw.ecommerceplatform.repositories.RoleRepository;
import com.tw.ecommerceplatform.services.JpaUserDetailsService;
import com.tw.ecommerceplatform.services.WarehouseService;
import com.tw.ecommerceplatform.validators.RegisterUserValidatorService;
import com.tw.ecommerceplatform.validators.RegisterWarehouseValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final JpaUserDetailsService userService;
    private final WarehouseService warehouseService;
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
            userService.registerUser(form, roleUser);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.user", "User already exists");
            return "register/registerCustomer";
        }
        return "redirect:/login";
    }


    // Register Warehouse & it's Admin
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

        // Try to register the warehouse and it's admin
        try {
            RoleEntity roleWarehouseAdmin = roleRepository.findByName("ROLE_WAREHOUSE_ADMIN");
            warehouseService.registerWarehouse(form, roleWarehouseAdmin);
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "Warehouse already exists")) {
                bindingResult.rejectValue("name", "error.warehouse", "Warehouse already exists");
            } else if (Objects.equals(e.getMessage(), "Warehouse already exists and is pending approval")) {
                bindingResult.rejectValue("name", "error.warehouse", "Warehouse already exists and is pending approval");
            } else if (Objects.equals(e.getMessage(), "User already exists"))
                bindingResult.rejectValue("username", "error.user", "User already exists");
            return "register/registerWarehouse";
        }

        // Redirect to pending page
        return "redirect:/register/warehouse/pending";
    }
}
