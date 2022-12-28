package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.*;
import com.tw.ecommerceplatform.models.ListOfOrderItemModel;
import com.tw.ecommerceplatform.models.OrderItemModel;
import com.tw.ecommerceplatform.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final WarehouseItemRepository warehouseItemRepository;
    private final ShopItemRepository shopItemRepository;

    public void placeOrder(WarehouseEntity warehouse,
                           ShopEntity shop,
                           List<OrderItemModel> orderItems,
                           ListOfOrderItemModel listOrderItemsModel) throws Exception {

        // Attach the name of the items to the order items
        for (int i = 0; i < orderItems.size(); i++) {
            listOrderItemsModel.getOrderItems().get(i).setItemName(orderItems.get(i).getItemName());
        }

        // filter the order items with quantity greater than 0
        List<OrderItemModel> filteredOrderItems = listOrderItemsModel.getOrderItems().stream()
                .filter(orderItem -> orderItem.getItemQuantity() > 0)
                .toList();

        // Check if the shop admin chose at least one item
        if (filteredOrderItems.size() > 0) {

            // Create a new order
            OrderEntity order = new OrderEntity();
            order.setWarehouse(warehouse);
            order.setShop(shop);
            orderRepository.save(order);

            // Check if the warehouse has enough units of items to fulfill the order
            for (OrderItemModel orderItem : filteredOrderItems) {

                WarehouseItem warehouseItem = warehouseItemRepository.findByWarehouseAndItem_Name(warehouse, orderItem.getItemName());
                if (warehouseItem.getQuantity() < orderItem.getItemQuantity()) {
                    throw new Exception("For item " + orderItem.getItemName()
                            + " the warehouse has only "
                            + warehouseItemRepository.findByWarehouseAndItem_Name(warehouse, orderItem.getItemName()).getQuantity()
                            + " units!");
                } else {
                    // Add the order item to the order
                    ItemEntity item = itemRepository.findByName(orderItem.getItemName());
                    OrderItem orderItemEntity = new OrderItem(item, order, orderItem.getItemQuantity());
                    orderItemRepository.save(orderItemEntity);

                    // Decrease the quantity of the item in the warehouse
                    warehouseItem.setQuantity(warehouseItem.getQuantity() - orderItem.getItemQuantity());
                    warehouseItemRepository.save(warehouseItem);

                    // Increase the quantity of the item in the shop
                    // Check if the shop already has the item
                    ShopItem existingShopItem = shopItemRepository.findByShopAndItem_Name(shop, orderItem.getItemName());
                    if (existingShopItem != null) {
                        existingShopItem.setQuantity(existingShopItem.getQuantity() + orderItem.getItemQuantity());
                        shopItemRepository.save(existingShopItem);

                        // If the shop doesn't have the item, add it
                    } else {
                        ShopItem newShopItem = new ShopItem(item, shop, orderItem.getItemQuantity());
                        shopItemRepository.save(newShopItem);
                    }
                }
            }
        } else {
            throw new Exception("Increase the quantity of at least one item!");
        }
    }
}
