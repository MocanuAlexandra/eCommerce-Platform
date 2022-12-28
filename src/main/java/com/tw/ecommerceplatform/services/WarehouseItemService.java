package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.ItemEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.entities.WarehouseItem;
import com.tw.ecommerceplatform.models.CreateItemModel;
import com.tw.ecommerceplatform.models.EditItemModel;
import com.tw.ecommerceplatform.repositories.ItemRepository;
import com.tw.ecommerceplatform.repositories.WarehouseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class WarehouseItemService {
    private final WarehouseItemRepository warehouseItemRepository;
    private final ItemRepository itemRepository;

    // Get all items in a warehouse
    public List<WarehouseItem> getItemsByWarehouse(WarehouseEntity warehouse) {
        return warehouseItemRepository.findByWarehouse(warehouse);
    }


    // Get item warehouse by item id
    public WarehouseItem getItemWarehouseByItemId(Long id, WarehouseEntity warehouse) {
        return warehouseItemRepository.findByItem_Id(warehouse, id);
    }

    // Save an item in a warehouse
    public void saveItemWarehouse(CreateItemModel createItemModel, WarehouseEntity warehouse) throws Exception {
        WarehouseItem warehouseItem = new WarehouseItem();

        // Check if the item already exists in the warehouse
        List<WarehouseItem> items = warehouseItemRepository.findByWarehouse(warehouse);
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
            warehouseItem.setItem(newItem);
            warehouseItem.setWarehouse(warehouse);
            warehouseItem.setQuantity(createItemModel.getQuantity());
        }
        // Save into db
        warehouseItemRepository.save(warehouseItem);
    }

    // Edit an item in a warehouse
    public void updateItemWarehouse(WarehouseItem updatedWarehouseItem, EditItemModel form) throws Exception {

        //TODO decom this when validate
//        if (form.getQuantity() < updatedItemWarehouse.getQuantity())
//            throw new Exception("Quantity cannot be less than the current quantity");
        updatedWarehouseItem.setQuantity(form.getQuantity());
        warehouseItemRepository.save(updatedWarehouseItem);
    }
}
