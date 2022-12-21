package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.ItemEntity;
import com.tw.ecommerceplatform.entities.ItemWarehouseEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.models.CreateItemModel;
import com.tw.ecommerceplatform.repositories.ItemRepository;
import com.tw.ecommerceplatform.repositories.ItemWarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ItemWarehouseService {
    private final ItemWarehouseRepository itemWarehouseRepository;
    private final ItemRepository itemRepository;

    // Get all items in a warehouse
    public List<ItemWarehouseEntity> getItemsByWarehouse(WarehouseEntity warehouse) {
        return itemWarehouseRepository.findByWarehouse(warehouse);
    }

    // Save an item in a warehouse
    public void saveItemWarehouse(CreateItemModel createItemModel, WarehouseEntity warehouse) throws Exception {
        ItemWarehouseEntity itemWarehouse = new ItemWarehouseEntity();

        // Check if the item already exists in the warehouse
        List<ItemWarehouseEntity> items = itemWarehouseRepository.findByWarehouse(warehouse);
        if (items.stream().anyMatch(item -> item.getItem().getName().equals(createItemModel.getName()))) {
            throw new Exception("Item already exists in the warehouse");
        } else {
            // Save the item in db
            ItemEntity item = new ItemEntity();
            item.setName(createItemModel.getName());
            itemRepository.save(item);

            // Save the item in warehouse
            itemWarehouse.setItem(item);
            itemWarehouse.setWarehouse(warehouse);
            itemWarehouse.setQuantity(createItemModel.getQuantity());
        }
        // Save into db
        itemWarehouseRepository.save(itemWarehouse);
    }
}
