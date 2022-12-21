package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.ItemEntity;
import com.tw.ecommerceplatform.entities.ItemWarehouseEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.services.ItemWarehouseService;
import com.tw.ecommerceplatform.services.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WarehouseController {
    private final ItemWarehouseService itemWarehouseService;
    private final WarehouseService warehouseService;

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

    // Endpoint to pending approval page
    @GetMapping("/register/warehouse/pending")
    public String pending() {
        return "register/registrationPending";
    }

    // Endpoint to increase the quantity of an item in the warehouse
    @PostMapping("/increase-quantity")
    public String increaseQuantity(@RequestParam Long itemId, @RequestParam Integer quantity) {
        ItemWarehouseEntity itemWarehouse = itemWarehouseService.getItemWarehouseByItemId(itemId);
        itemWarehouse.setQuantity(itemWarehouse.getQuantity() + quantity);
        itemWarehouseService.save(itemWarehouse);

        return "redirect:/warehouse";
    }
}
