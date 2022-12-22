package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.ItemEntity;
import com.tw.ecommerceplatform.entities.ItemWarehouse;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.models.CreateItemModel;
import com.tw.ecommerceplatform.models.EditItemModel;
import com.tw.ecommerceplatform.repositories.ItemWarehouseRepository;
import com.tw.ecommerceplatform.services.ItemService;
import com.tw.ecommerceplatform.services.ItemWarehouseService;
import com.tw.ecommerceplatform.services.WarehouseService;
import com.tw.ecommerceplatform.validators.CreatItemValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WarehouseController {
    private final ItemService itemService;
    private final ItemWarehouseService itemWarehouseService;
    private final WarehouseService warehouseService;
    private final CreatItemValidatorService createEditItemValidatorService;
    private final ItemWarehouseRepository itemWarehouseRepository;

    // Endpoint to pending approval page
    @GetMapping("/register/warehouse/pending")
    public String pending() {
        return "register/registrationPending";
    }

    // TODO add pending orders from shop to warehouse
    // Endpoint to main page of warehouse' admin
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @RequestMapping("/warehouse")
    public String getWarehouseAdminPage(Model model,
                                        Authentication authentication) {

        // Get the warehouse id by the username of the logged in user
        String username = authentication.getName();
        WarehouseEntity warehouse = warehouseService.getWarehouseByAdminEmail(username);

        // Get all items in the warehouse along with their quantity
        List<ItemWarehouse> itemWarehouses = itemWarehouseService.getItemsByWarehouse(warehouse);

        List<ItemEntity> items = itemWarehouses.stream()
                .map(ItemWarehouse::getItem)
                .collect(Collectors.toList());

        Map<ItemEntity, Integer> itemQuantities = itemWarehouses.stream()
                .collect(Collectors.toMap(ItemWarehouse::getItem, ItemWarehouse::getQuantity));

        // Add the items and their quantity to the model
        // Also add the warehouse to the model
        model.addAttribute("items", items);
        model.addAttribute("itemQuantities", itemQuantities);
        model.addAttribute("warehouse", warehouse);
        return "warehouse/warehousePanel";
    }


    // Endpoint to create a new item page
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @GetMapping("/warehouse/createItem")
    public String getCreateItemPage(Model model) {
        model.addAttribute("form", new CreateItemModel());
        return "warehouse/createItem";
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/warehouse/createItem")
    public String createItem(@ModelAttribute("form") CreateItemModel form,
                             Authentication authentication,
                             BindingResult bindingResult) {

        // Validate the form
        createEditItemValidatorService.validate(form, bindingResult);
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
            } else if (e.getMessage().equals("Item already exists in another warehouse")) {
                bindingResult.rejectValue("name", "error.form",
                        "Item already exists in another warehouse. Try a different name");
            }
            return "warehouse/createItem";
        }

        // Redirect to the warehouse admin page
        return "redirect:/warehouse";
    }


    // Endpoint to update an item
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @GetMapping("/warehouse/editItem/{itemId}")
    public String getEditItemPage(@PathVariable Long itemId,
                                  Model model,
                                  Authentication authentication) {

        // Get the warehouse id by the username of the logged in user
        String username = authentication.getName();
        WarehouseEntity warehouse = warehouseService.getWarehouseByAdminEmail(username);

        // Get the itemWarehouse object by the item id and warehouse id
        ItemWarehouse itemWarehouse = itemWarehouseService.getItemWarehouseByItemId(itemId, warehouse);

        // Create a new edit item form object and set the id, name and quantity
        EditItemModel form = new EditItemModel();
        form.setId(itemId);
        form.setName(itemWarehouse.getItem().getName());
        form.setQuantity(itemWarehouse.getQuantity());

        model.addAttribute("form", form);
        return "warehouse/editItem";
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/warehouse/editItem/{itemId}")
    public String editItem(@PathVariable Long itemId,
                           @ModelAttribute EditItemModel form,
                           Authentication authentication,
                           BindingResult bindingResult) {

        // TODO validate the edit item form

        try {
            // Get the item object
            ItemEntity updatedItem = itemService.getById(itemId);

            // Get the itemWarehouse object
            String username = authentication.getName();
            WarehouseEntity warehouse = warehouseService.getWarehouseByAdminEmail(username);
            ItemWarehouse updatedItemWarehouse = itemWarehouseService.getItemWarehouseByItemId(itemId, warehouse);

            // Update the item object
            if (updatedItem != null) {
                itemService.updateItem(updatedItem, form);
            }

            // Update the itemWarehouse object
            if (updatedItemWarehouse != null) {
                itemWarehouseService.updateItemWarehouse(updatedItemWarehouse, form);
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Item already exists in another warehouse")) {
                bindingResult.rejectValue("name", "error.form",
                        "Item already exists in another warehouse. Try a different name");
            } else if (e.getMessage().equals("Quantity cannot be less than the current quantity")) {
                bindingResult.rejectValue("quantity", "error.form",
                        "Quantity cannot be less than the current quantity");
            }
            return "warehouse/editItem";
        }

        // Redirect to the warehouse admin page
        return "redirect:/warehouse";
    }
}
