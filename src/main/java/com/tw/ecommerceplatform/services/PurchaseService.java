package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.*;
import com.tw.ecommerceplatform.models.PurchaseCartModel;
import com.tw.ecommerceplatform.models.PurchaseItemModel;
import com.tw.ecommerceplatform.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;
    private final ShopItemRepository shopItemRepository;

    public void purchase(String username, List<PurchaseItemModel> purchaseItems, PurchaseCartModel purchaseCartModel) throws Exception {

        // Get the customer
        UserEntity customerEntity = userRepository.findByEmail(username);

        // Attach the name of the items adn shops to the purchase items
        for (int i = 0; i < purchaseItems.size(); i++) {
            purchaseCartModel.getPurchaseItems().get(i).setItemName(purchaseItems.get(i).getItemName());
            purchaseCartModel.getPurchaseItems().get(i).setShopName(purchaseItems.get(i).getShopName());
        }

        // filter the order items with quantity greater than 0
        List<PurchaseItemModel> filteredPurchaseItems = purchaseCartModel.getPurchaseItems().stream()
                .filter(purchaseItem -> purchaseItem.getItemQuantity() > 0)
                .toList();

        // Check if the customer chose at least one item
        if (filteredPurchaseItems.size() > 0) {

            // Create a new purchase with shops list empty
            PurchaseEntity purchase = new PurchaseEntity();
            purchase.setCustomer(customerEntity);
            purchase.setShops(new ArrayList<>());
            purchaseRepository.save(purchase);
            List<ShopEntity> shops = new ArrayList<>();

            for (PurchaseItemModel purchaseItem : filteredPurchaseItems) {

                //Get the shop
                ShopEntity requiredShop = shopRepository.findByName(purchaseItem.getShopName());

                //Get the shop item
                ShopItem shopItem = requiredShop.getItems().stream()
                        .filter(item -> item.getItem().getName().equals(purchaseItem.getItemName()))
                        .findFirst()
                        .get();

                // Check if the shop has enough units to fulfill the order
                if (shopItem.getQuantity() < purchaseItem.getItemQuantity()) {
                    throw new RuntimeException("For item " + purchaseItem.getItemName()
                            + " the shop " + purchaseItem.getShopName() + " has only "
                            + shopItem.getQuantity()
                            + " units!");
                } else {
                    // Add the purchase item to the purchase
                    ItemEntity item = itemRepository.findByName(purchaseItem.getItemName());
                    PurchaseItem purchaseItemEntity = new PurchaseItem(item, purchase, purchaseItem.getItemQuantity());
                    purchaseItemRepository.save(purchaseItemEntity);

                    // Add the shop to the list of shops that will be added to the purchase
                    shops.add(requiredShop);

                    // Update the shop item quantity
                    shopItem.setQuantity(shopItem.getQuantity() - purchaseItem.getItemQuantity());
                    shopItemRepository.save(shopItem);
                }
            }

            // Add the shops to the purchase
            PurchaseEntity existingPurchase = purchaseRepository.findById(purchase.getId());
            if (existingPurchase != null) {
                existingPurchase.setShops(shops);
                purchaseRepository.save(existingPurchase);
            }
        } else {
            throw new Exception("Increase the quantity of at least one item!");
        }
    }
}
