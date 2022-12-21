package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.ItemEntity;
import com.tw.ecommerceplatform.entities.ItemWarehouseEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.models.CreateItemModel;
import com.tw.ecommerceplatform.services.ItemWarehouseService;
import com.tw.ecommerceplatform.services.WarehouseService;
import com.tw.ecommerceplatform.validators.CreateItemValidatorService;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WarehouseController {
    private final ItemWarehouseService itemWarehouseService;
    private final WarehouseService warehouseService;

    private final CreateItemValidatorService createItemValidatorService;

    // Endpoint to pending approval page
    @GetMapping("/register/warehouse/pending")
    public String pending() {
        return "register/registrationPending";
    }

    // Endpoint to main page of warehouse' admin
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @RequestMapping("/warehouse")
    public String getWarehouseAdminPage(Model model,
                                        Authentication authentication) {

        // Get the warehouse id by the username of the logged in user
        String username = authentication.getName();
        WarehouseEntity warehouse = warehouseService.getWarehouseByAdminEmail(username);

        // Get all items in the warehouse along with their quantity
        List<ItemWarehouseEntity> itemWarehouses = itemWarehouseService.getItemsByWarehouse(warehouse);

        List<ItemEntity> items = itemWarehouses.stream()
                .map(ItemWarehouseEntity::getItem)
                .collect(Collectors.toList());

        Map<ItemEntity, Integer> itemQuantities = itemWarehouses.stream()
                .collect(Collectors.toMap(ItemWarehouseEntity::getItem, ItemWarehouseEntity::getQuantity));

        // Add the items and their quantity to the model
        model.addAttribute("items", items);
        model.addAttribute("itemQuantities", itemQuantities);

        return "warehouse/warehousePanel";
    }

    // Endpoint to create a new item page
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @GetMapping("/warehouse/createItem")
    public String getChangePassword(Model model) {
        model.addAttribute("form", new CreateItemModel());
        return "warehouse/createItem";
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/warehouse/createItem")
    public String createItem(@ModelAttribute("form") CreateItemModel form,
                             Authentication authentication,
                             BindingResult bindingResult) throws Exception {

        // Validate the form
        createItemValidatorService.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "warehouse/createItem";
        }

        try {
            // Get the warehouse id by the username of the logged-in user
            WarehouseEntity warehouse = warehouseService.getWarehouseByAdminEmail(authentication.getName());

            //Add the item to the warehouse
            itemWarehouseService.saveItemWarehouse(form, warehouse);
        } catch (Exception e) {
            if (e.getMessage().equals("Item already exists in the warehouse")) {
                bindingResult.rejectValue("name", "error.form", "Item already exists in the warehouse");
                return "warehouse/createItem";
            }
        }

        return "redirect:/warehouse";
    }

    // TODO: Add endpoint to update an item (increase quantity)
}
