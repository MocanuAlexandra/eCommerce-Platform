package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.ShopEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.services.ShopService;
import com.tw.ecommerceplatform.services.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {
    private final WarehouseService warehouseService;
    private final ShopService shopService;

    // TODO add it own inventory

    // Endpoint to main page of shop' admin
    @PreAuthorize("hasRole('SHOP_ADMIN')")
    @RequestMapping("/shop")
    public String getAdminShopPage(Model model,
                                   Authentication authentication) {

        //Add the warehouses with approved state to the model
        List<WarehouseEntity> warehouses = warehouseService.getAllApprovedWarehouses();
        model.addAttribute("warehouses", warehouses);

        //Get the shop name by the username of the logged in user
        String username = authentication.getName();
        ShopEntity shop = shopService.getShopByAdminEmail(username);
        model.addAttribute("shop", shop);

        return "shop/shop";
    }

    // Endpoint to pending approval page
    @GetMapping("/register/shop/pending")
    public String pending() {
        return "register/registrationPending";
    }
}
