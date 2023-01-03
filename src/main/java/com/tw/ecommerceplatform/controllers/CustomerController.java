package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.ShopEntity;
import com.tw.ecommerceplatform.entities.ShopItem;
import com.tw.ecommerceplatform.models.PurchaseCartModel;
import com.tw.ecommerceplatform.models.PurchaseItemModel;
import com.tw.ecommerceplatform.services.PurchaseService;
import com.tw.ecommerceplatform.services.ShopItemService;
import com.tw.ecommerceplatform.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@SessionAttributes("purchaseItems")
public class CustomerController {
    private final ShopItemService shopItemService;
    private final ShopService shopService;

    private final PurchaseService purchaseService;

    // Endpoint to thank you page for ordering
    @GetMapping("/customer/thankYou")
    public String thankYouPage() {
        return "customer/thankYouPage";
    }

    // Endpoint to main page of customer
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

    // Get all items from all shops according to the search term
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

    //Add items to cart ->redirect to cart page
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequestMapping("/customer/purchase")
    public String getCartPage(@RequestParam(name = "purchaseItemsId", required = false) List<Long> purchaseItemsId,
                              Model model) {

        // Check if the purchase item list is empty
        // If it is empty, reload the page
        if (purchaseItemsId == null || purchaseItemsId.isEmpty()) {
            return "redirect:/customer";
        } else {
            // Get all the shop items according to the purchase item list
            List<ShopItem> shopItems = shopItemService.getAllItemsById(purchaseItemsId);

            // Transform the shop items to purchase items
            List<PurchaseItemModel> purchaseItems = new ArrayList<>();
            for (ShopItem shopItem : shopItems) {
                purchaseItems.add(new PurchaseItemModel(shopItem.getItem().getName(), shopItem.getShop().getName(), 0));
            }

            // Add the purchase items into the purchase cart model
            PurchaseCartModel purchaseCartModel = new PurchaseCartModel();
            for (PurchaseItemModel purchaseItem : purchaseItems) {
                purchaseCartModel.addToPurchaseCart(purchaseItem);
            }

            //Add to the model
            model.addAttribute("purchaseCartModel", purchaseCartModel);
            model.addAttribute("purchaseItems", purchaseItems);

            return "customer/cart";
        }
    }

    // Endpoint to place a purchase
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/customer/purchase")
    public String placePurchase(
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("purchaseItems") List<PurchaseItemModel> purchaseItems,
            @ModelAttribute("purchaseCartModel") PurchaseCartModel purchaseCartModel,
            Model model) {

        //Get the user
        String customer = authentication.getName();

        //Try to purchase
        try {
            purchaseService.purchase(customer, purchaseItems, purchaseCartModel);
            // Handle the exception
        } catch (Exception e) {

            model.addAttribute("errorMessage", e.getMessage());
            return "customer/cart";
        }

        // After placing the order, redirect to thank you page
        return "redirect:/customer/thankYou";
    }
}
