package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.ItemEntity;
import com.tw.ecommerceplatform.entities.ItemWarehouse;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.models.CreateItemModel;
import com.tw.ecommerceplatform.models.EditItemModel;
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
    public List<ItemWarehouse> getItemsByWarehouse(WarehouseEntity warehouse) {
        return itemWarehouseRepository.findByWarehouse(warehouse);
    }


    // Get item warehouse by item id
    public ItemWarehouse getItemWarehouseByItemId(Long id, WarehouseEntity warehouse) {
        return itemWarehouseRepository.findByItem_Id(warehouse, id);
    }

    // Save an item in a warehouse
    public void saveItemWarehouse(CreateItemModel createItemModel, WarehouseEntity warehouse) throws Exception {
        ItemWarehouse itemWarehouse = new ItemWarehouse();

        // Check if the item already exists in the warehouse
        List<ItemWarehouse> items = itemWarehouseRepository.findByWarehouse(warehouse);
        if (items.stream().anyMatch(item -> item.getItem().getName().equals(createItemModel.getName()))) {
            throw new Exception("Item already exists in the warehouse");
        } else {

            //Check if the item already exists in the database (so is in another warehouse)
            ItemEntity savedItem = itemRepository.findByName(createItemModel.getName());
            ItemEntity newItem = new ItemEntity();

            if (savedItem != null)
                throw new Exception("Item already exists in another warehouse");
            else {
                newItem.setName(createItemModel.getName());
                itemRepository.save(newItem);
            }

            // Save the item in warehouse
            itemWarehouse.setItem(newItem);
            itemWarehouse.setWarehouse(warehouse);
            itemWarehouse.setQuantity(createItemModel.getQuantity());
        }
        // Save into db
        itemWarehouseRepository.save(itemWarehouse);
    }

    // Edit an item in a warehouse
    public void updateItemWarehouse(ItemWarehouse updatedItemWarehouse, EditItemModel form) throws Exception {

        if (form.getQuantity() < updatedItemWarehouse.getQuantity())
            throw new Exception("Quantity cannot be less than the current quantity");
        updatedItemWarehouse.setQuantity(form.getQuantity());
        itemWarehouseRepository.save(updatedItemWarehouse);
    }
}
