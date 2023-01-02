package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.ShopItem;
import com.tw.ecommerceplatform.services.ShopItemService;
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
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequestMapping("/customer")
    public String getCustomerPanelPage(Model model) {

        // Get all items from all shops
        List<ShopItem> shopItems = shopItemService.getAllItems();

        model.addAttribute("shopItems", shopItems);
        return "customer/customerPanel";
    }

    @GetMapping("/search")
    public String search(@RequestParam("searchedItem") String searchedItem, Model model) {

        // Get all items from all shops according to the search term
        List<ShopItem> shopItems = shopItemService.getSearchItem(searchedItem);
        model.addAttribute("shopItems", shopItems);

        return "customer/customerPanel";
    }
}
