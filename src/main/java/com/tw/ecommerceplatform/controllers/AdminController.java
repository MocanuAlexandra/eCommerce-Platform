package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.utility.RegistrationStatus;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.models.ChangePasswordUserModel;
import com.tw.ecommerceplatform.services.JpaUserDetailsService;
import com.tw.ecommerceplatform.services.WarehouseService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final ChangePasswordValidatorService changePasswordValidatorService;
    private final JpaUserDetailsService userService;
    private final WarehouseService warehouseService;

    // Endpoint to main page of admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private")
    public String open(Model model) {

        // Add the warehouses with pending state to the model
        List<WarehouseEntity> warehouses = warehouseService.getAllPendingWarehouses();
        model.addAttribute("warehouses", warehouses);

        return "admin/admin";
    }

    //TODO make 2 tabs for pending and approved warehouses/shops
    //TODO shop admin
    //Endpoint to approve/reject registrations coming from warehouse/shop admin
    @PostMapping("/approve-reject")
    public String approveReject(@RequestParam("action") String action, @RequestParam("id") Long id, Model model) {
        if (action.equalsIgnoreCase(RegistrationStatus.APPROVED.getName())) {

            // Approve registration
            WarehouseEntity warehouse = warehouseService.getWarehouseById(id);
            warehouse.getAdminWarehouse().setStatus(RegistrationStatus.APPROVED);
            warehouseService.saveWarehouse(warehouse);

        } else if (action.equalsIgnoreCase(RegistrationStatus.REJECTED.getName())) {

            // Reject registration -> remove both warehouse and it's admin from db
            Long userId = warehouseService.getWarehouseById(id).getAdminWarehouse().getId();
            warehouseService.deleteWarehouse(id);
            userService.deleteUser(userId);
        }

        List<WarehouseEntity> warehouses = warehouseService.getAllPendingWarehouses();
        model.addAttribute("warehouses", warehouses);
        return "admin/admin";
    }


    // Endpoint to change password
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("form", new ChangePasswordUserModel());
        return "admin/changePassword";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/changePassword")
    public String changePassword(@ModelAttribute("form") ChangePasswordUserModel form,
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