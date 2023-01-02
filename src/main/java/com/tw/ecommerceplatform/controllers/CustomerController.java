package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.ShopEntity;
import com.tw.ecommerceplatform.entities.ShopItem;
import com.tw.ecommerceplatform.services.ShopItemService;
import com.tw.ecommerceplatform.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final ShopItemService shopItemService;
    private final ShopService shopService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @RequestMapping("/customer")
    public String getCustomerPanelPage(Model model) {

        // Get all items from all shops
        List<ShopItem> shopItems = shopItemService.getAllItems();
        // Get all the shops
        List<ShopEntity> shops = shopService.getAllItems();

        model.addAttribute("shops", shops);
        model.addAttribute("shopItems", shopItems);

        return "customer/customerPanel";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/search")
    public String searchItem(Model model,
                             @RequestParam("searchedItem") String searchedItem,
                             @RequestParam(name = "requiredShop", required = false) List<Long> requiredShop) {


        // Check if the search term is empty and if the shop list is empty
        // If both are empty, reload the page
        if ((searchedItem == null || searchedItem.isEmpty()) && (requiredShop == null || requiredShop.isEmpty())) {
            return "redirect:/customer";
        } else {
            // Get all items from all shops according to the search term
            List<ShopItem> shopItems = shopItemService.getSearchItem(searchedItem, requiredShop);
            model.addAttribute("shopItems", shopItems);

            // Get all the shops
            List<ShopEntity> shops = shopService.getAllItems();
            model.addAttribute("shops", shops);

            return "customer/customerPanel";
        }
    }
}
