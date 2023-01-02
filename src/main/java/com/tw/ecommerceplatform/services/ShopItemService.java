package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.ShopEntity;
import com.tw.ecommerceplatform.entities.ShopItem;
import com.tw.ecommerceplatform.repositories.ShopItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopItemService {
    private final ShopItemRepository shopItemRepository;

    // Get all items in a shop
    public List<ShopItem> getItemsByShop(ShopEntity shop) {
        return shopItemRepository.findByShop(shop);
    }

    // Get all items from all shops
    public List<ShopItem> getAllItems() {
        return shopItemRepository.findAll();
    }

    // Get all items from all shops according to the search term
    public List<ShopItem> getSearchItem(String searchedItem) {
        return shopItemRepository.findByItemNameContainingIgnoreCase(searchedItem);
    }
}
