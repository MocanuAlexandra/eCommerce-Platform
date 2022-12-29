package com.tw.ecommerceplatform.services;
import com.tw.ecommerceplatform.entities.ItemEntity;
import com.tw.ecommerceplatform.models.EditItemModel;
import com.tw.ecommerceplatform.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    // Update item
    public void updateItem(ItemEntity item, EditItemModel form) throws Exception {

        //Check if the item already exists in the database (so is in another warehouse)
        ItemEntity savedItem = itemRepository.findByName(form.getName());
        if (savedItem != null && savedItem.getId() != item.getId())
            throw new Exception("Item already exists in another warehouse. Try a different name.");
        else {
            item.setName(form.getName());
            itemRepository.save(item);
        }
    }

    // Get item by id
    public ItemEntity getById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }
}
