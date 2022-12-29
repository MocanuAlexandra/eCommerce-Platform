package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.ShopEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.models.ChangePasswordUserModel;
import com.tw.ecommerceplatform.services.JpaUserDetailsService;
import com.tw.ecommerceplatform.services.ShopService;
import com.tw.ecommerceplatform.services.ShopWarehouseContractService;
import com.tw.ecommerceplatform.services.WarehouseService;
import com.tw.ecommerceplatform.utility.RegistrationStatus;
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
    private final ShopService shopService;
    private final ShopWarehouseContractService shopWarehouseContractService;

    // Endpoint to main page of admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private")
    public String getAdminPanel(Model model) {

        //Add the warehouses with registration pending state to the model
        List<WarehouseEntity> warehouses = warehouseService.getAllPendingWarehouses();
        model.addAttribute("warehouses", warehouses);

        // Add the shops with registration pending state to the model
        List<ShopEntity> shops = shopService.getAllPendingShops();
        model.addAttribute("shops", shops);

        return "admin/admin";
    }

    //Endpoint to approve/reject registrations coming from warehouse admin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("warehouse/approve-reject")
    public String approveRejectWarehouse(@RequestParam("action") String action,
                                         @RequestParam("id") Long id,
                                         Model model) {
        if (action.equalsIgnoreCase(RegistrationStatus.APPROVED.getName())) {

            // Approve registration
            warehouseService.approveRegistration(id);

        } else if (action.equalsIgnoreCase(RegistrationStatus.REJECTED.getName())) {

            // Reject registration -> remove both warehouse, and it's admin from db
            warehouseService.rejectWarehouse(id);
        }

        // Reload the warehouses with registration pending state  to the model
        List<WarehouseEntity> warehouses = warehouseService.getAllPendingWarehouses();
        model.addAttribute("warehouses", warehouses);

        return "redirect:/private";
    }


    //Endpoint to approve/reject registrations coming from shop admin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("shop/approve-reject")
    public String approveRejectShop(@RequestParam("action") String action,
                                    @RequestParam("id") Long id,
                                    Model model) {
        if (action.equalsIgnoreCase(RegistrationStatus.APPROVED.getName())) {

            // Approve registration
            shopService.approveShop(id);

            // Add non-existing contracts between this shop and all warehouses approved by admin
            shopWarehouseContractService.addNonExistingContracts(id);

        } else if (action.equalsIgnoreCase(RegistrationStatus.REJECTED.getName())) {

            // Reject registration -> remove both shop and it's admin from db
            shopService.rejectRegistration(id);
        }

        // Reload the shops with pending state to the model
        List<ShopEntity> shops = shopService.getAllPendingShops();
        model.addAttribute("shops", shops);

        return "redirect:/private";
    }


    // Endpoint to change password
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private/changePassword")
    public String getChangePassword(Model model) {
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