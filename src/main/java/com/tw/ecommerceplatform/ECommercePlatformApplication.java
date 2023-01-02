package com.tw.ecommerceplatform;

import com.tw.ecommerceplatform.entities.*;
import com.tw.ecommerceplatform.repositories.*;
import com.tw.ecommerceplatform.utility.ContractStatus;
import com.tw.ecommerceplatform.utility.RegistrationStatus;
import com.tw.ecommerceplatform.utility.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ECommercePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommercePlatformApplication.class, args);
    }

    //TODO REMOVE THIS
    @Bean
    public CommandLineRunner insert(RoleRepository roleRepository,
                                    UserRepository userRepository,
                                    WarehouseRepository warehouseRepository,
                                    ShopRepository shopRepository,
                                    ItemRepository itemRepository,
                                    WarehouseItemRepository warehouseItemRepository,
                                    ShopItemRepository shopItemRepository,
                                    ShopWarehouseContractRepository shopWarehouseContractRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {
            RoleEntity role_user = new RoleEntity(Role.CUSTOMER.getName());
            RoleEntity role_admin = new RoleEntity(Role.ADMIN.getName());
            RoleEntity role_warehouse_admin = new RoleEntity(Role.WAREHOUSE_ADMIN.getName());
            RoleEntity role_shop_admin = new RoleEntity(Role.SHOP_ADMIN.getName());
            roleRepository.save(role_user);
            roleRepository.save(role_admin);
            roleRepository.save(role_warehouse_admin);
            roleRepository.save(role_shop_admin);


            UserEntity user = new UserEntity("username@gmail.com",
                    passwordEncoder.encode("password"), role_user, RegistrationStatus.APPROVED);
            UserEntity admin = new UserEntity("admin@gmail.com",
                    passwordEncoder.encode("admin"), role_admin, RegistrationStatus.APPROVED);
            UserEntity warehouse_admin = new UserEntity("warehouse@gmail.com",
                    passwordEncoder.encode("admin"), role_warehouse_admin, RegistrationStatus.APPROVED);
            UserEntity warehouse_admin2 = new UserEntity("warehouse2@gmail.com",
                    passwordEncoder.encode("admin"), role_warehouse_admin, RegistrationStatus.APPROVED);
            UserEntity shop_admin = new UserEntity("shop@gmail.com",
                    passwordEncoder.encode("admin"), role_shop_admin, RegistrationStatus.APPROVED);
            UserEntity shop_admin2 = new UserEntity("shop2@gmail.com",
                    passwordEncoder.encode("admin"), role_shop_admin, RegistrationStatus.APPROVED);
            userRepository.save(user);
            userRepository.save(admin);
            userRepository.save(warehouse_admin);
            userRepository.save(warehouse_admin2);
            userRepository.save(shop_admin);
            userRepository.save(shop_admin2);

            WarehouseEntity warehouse = new WarehouseEntity("Metro", "Street 56", "12345",
                    warehouse_admin);
            WarehouseEntity warehouse2 = new WarehouseEntity("Selgros", "Street 57", "12385",
                    warehouse_admin2);
            warehouseRepository.save(warehouse);
            warehouseRepository.save(warehouse2);

            ShopEntity shop = new ShopEntity("Lidl", "Strada 34", "12345", shop_admin);
            shopRepository.save(shop);
            ShopEntity shop2 = new ShopEntity("Carrefour", "Strada 35", "12345", shop_admin2);
            shopRepository.save(shop2);

            ItemEntity item = new ItemEntity("Flour");
            ItemEntity item2 = new ItemEntity("Sugar");
            ItemEntity item3 = new ItemEntity("Salt");
            itemRepository.save(item);
            itemRepository.save(item2);
            itemRepository.save(item3);

            WarehouseItem warehouseItem = new WarehouseItem(item, warehouse, 100);
            WarehouseItem warehouseItem2 = new WarehouseItem(item2, warehouse, 120);
            WarehouseItem warehouseItem3 = new WarehouseItem(item3, warehouse2, 10);
            warehouseItemRepository.save(warehouseItem);
            warehouseItemRepository.save(warehouseItem2);
            warehouseItemRepository.save(warehouseItem3);

            ShopItem shopItem = new ShopItem(item, shop, 10);
            ShopItem shopItem2 = new ShopItem(item2, shop2, 12);
            shopItemRepository.save(shopItem);
            shopItemRepository.save(shopItem2);

            ShopWarehouseContract shopWarehouseContract = new ShopWarehouseContract(shop, warehouse, ContractStatus.APPROVED);
            ShopWarehouseContract shopWarehouseContract2 = new ShopWarehouseContract(shop, warehouse2, ContractStatus.PENDING);
            shopWarehouseContractRepository.save(shopWarehouseContract);
            shopWarehouseContractRepository.save(shopWarehouseContract2);
        };
    }
}
