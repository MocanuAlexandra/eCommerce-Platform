package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.entities.*;
import com.tw.ecommerceplatform.models.OrderCartModel;
import com.tw.ecommerceplatform.models.OrderItemModel;
import com.tw.ecommerceplatform.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@SessionAttributes("orderItems")
public class ShopController {
    private final WarehouseService warehouseService;
    private final WarehouseItemService warehouseItemService;
    private final ShopService shopService;
    private final ShopItemService shopItemService;
    private final OrderService orderService;
    private final ShopWarehouseContractService shopWarehouseContractService;

    // Endpoint to pending approval page for registration
    @GetMapping("/register/shop/pending")
    public String pendingREgistrationPage() {
        return "register/pendingRegistrationPage";
    }

    // Endpoint to thank you page for ordering
    @GetMapping("/shop/thankYou")
    public String thankYouPage() {
        return "shop/thankYouPage";
    }

    // Endpoint to pending approval page for contract request
    @GetMapping("/shop/requestContract/pending")
    public String pendingContractPage() {
        return "shop/pendingContractPage";
    }

    // Endpoint to main page of shop' admin
    @PreAuthorize("hasRole('SHOP_ADMIN')")
    @RequestMapping("/shop")
    public String getAdminShopPage(Model model,
                                   Authentication authentication) {

        // Add the providers to the model
        // A provider is a warehouse that has a contract with the shop with 'approved' status
        List<ShopWarehouseContract> approvedContracts = shopWarehouseContractService.
                getAllApprovedContracts(shopService.getShopByAdminEmail(authentication.getName()));
        List<WarehouseEntity> providers = approvedContracts.stream()
                .map(ShopWarehouseContract::getWarehouse)
                .toList();

        // Add all the available warehouses to the model
        // A warehouse is available if it has a contract with the shop with 'non-exiting' status
        List<ShopWarehouseContract> nonExistingContracts = shopWarehouseContractService.
                getAllNonExistingContracts(shopService.getShopByAdminEmail(authentication.getName()));
        List<WarehouseEntity> warehouses = nonExistingContracts.stream()
                .map(ShopWarehouseContract::getWarehouse)
                .toList();

        //Get the shop name by the username of the logged in user
        String username = authentication.getName();
        ShopEntity shop = shopService.getShopByAdminEmail(username);

        //Get all items in the shop along with their quantity
        List<ShopItem> shopItems = shopItemService.getItemsByShop(shop);
        List<ItemEntity> items = shopItems.stream()
                .map(ShopItem::getItem)
                .toList();
        Map<ItemEntity, Integer> itemQuantities = shopItems.stream()
                .collect(Collectors.toMap(ShopItem::getItem, ShopItem::getQuantity));

        // Add the items and their quantity to the model
        // Also add the shop to the model, the providers and the available warehouses
        model.addAttribute("items", items);
        model.addAttribute("itemQuantities", itemQuantities);
        model.addAttribute("shop", shop);
        model.addAttribute("providers", providers);
        model.addAttribute("warehouses", warehouses);

        return "shop/shopPanel";
    }

    // Endpoint to request a contract with a warehouse
    @PreAuthorize("hasRole('SHOP_ADMIN')")
    @RequestMapping("/shop/requestContract/{warehouseId}")
    public String requestContract(@PathVariable Long warehouseId,
                                  Authentication authentication) {

        // Get the shop by the username of the logged in user
        ShopEntity shop = shopService.getShopByAdminEmail(authentication.getName());

        // Get the warehouse by the id
        WarehouseEntity warehouse = warehouseService.getWarehouseById(warehouseId);

        // Modify the contract with 'pending' status
        shopWarehouseContractService.saveContract(shop, warehouse);

        return "redirect:/shop/requestContract/pending";
    }


    // Endpoint to place an order to a certain warehouse -> get page
    @PreAuthorize("hasRole('SHOP_ADMIN')")
    @GetMapping("/shop/placeOrder/{warehouseId}")
    public String getWarehousePage(@PathVariable Long warehouseId,
                                   Model model) {

        // Get the warehouse by id
        WarehouseEntity warehouse = warehouseService.getWarehouseById(warehouseId);

        // Get all items in the warehouse
        List<WarehouseItem> itemsInWarehouse = warehouseItemService.getItemsByWarehouse(warehouse);
        List<ItemEntity> items = itemsInWarehouse.stream()
                .map(WarehouseItem::getItem)
                .toList();

        // Add the items in a list of OrderItemModel to the model
        List<OrderItemModel> orderItemsModel = new ArrayList<>();
        for (ItemEntity item : items) {
            orderItemsModel.add(new OrderItemModel(item.getName(), 0));
        }

        // Add the order items into the order cart model
        OrderCartModel orderCartModel = new OrderCartModel();
        for (OrderItemModel orderItem : orderItemsModel) {
            orderCartModel.addToOrderCart(orderItem);
        }

        // Add to the model
        model.addAttribute("orderCartModel", orderCartModel);
        model.addAttribute("orderItems", orderItemsModel);
        model.addAttribute("warehouse", warehouse);

        return "shop/placeOrder";
    }

    // Endpoint to place an order to a certain warehouse
    @PreAuthorize("hasRole('SHOP_ADMIN')")
    @PostMapping("/shop/placeOrder/{warehouseId}")
    public String placeOrder(@PathVariable Long warehouseId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes,
                             @ModelAttribute("orderItems") List<OrderItemModel> orderItems,
                             @ModelAttribute("orderCartModel") OrderCartModel orderCartModel) {

        // Get the warehouse by id
        WarehouseEntity warehouse = warehouseService.getWarehouseById(warehouseId);

        // Get the shop by the username of the logged in user
        String username = authentication.getName();
        ShopEntity shop = shopService.getShopByAdminEmail(username);

        // Try to place the order
        try {
            orderService.placeOrder(warehouse, shop, orderItems, orderCartModel);

            //Handle errors if the order cannot be placed
        } catch (Exception e) {
            redirectAttributes
                    .addAttribute("warehouseId", warehouseId)
                    .addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/shop/placeOrder/" + warehouseId;
        }

        // After placing the order, redirect to the shop page
        return "redirect:/shop/thankYou";
    }
}
