package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.ItemWarehouseEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.repositories.ItemWarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ItemWarehouseService {
    private final ItemWarehouseRepository itemWarehouseRepository;

    // Get all items in a warehouse
    public List<ItemWarehouseEntity> getItemsByWarehouse(WarehouseEntity warehouse) {
        return itemWarehouseRepository.findByWarehouse(warehouse);
    }

    // Get the item warehouse entity by item id
    public ItemWarehouseEntity getItemWarehouseByItemId(Long itemId) {
        return itemWarehouseRepository.findByItem(itemId);
    }

    public void save(ItemWarehouseEntity itemWarehouse) {
        itemWarehouseRepository.save(itemWarehouse);
    }
}
