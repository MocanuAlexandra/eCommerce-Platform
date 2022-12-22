package com.tw.ecommerceplatform;

import com.tw.ecommerceplatform.entities.*;
import com.tw.ecommerceplatform.repositories.*;
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
                                    ItemWarehouseRepository itemWarehouseRepository,
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
                    passwordEncoder.encode("admin"), role_warehouse_admin, RegistrationStatus.PENDING);
            UserEntity shop_admin = new UserEntity("shop@gmail.com",
                    passwordEncoder.encode("admin"), role_shop_admin, RegistrationStatus.APPROVED);
            UserEntity shop_admin2 = new UserEntity("shop2@gmail.com",
                    passwordEncoder.encode("admin"), role_shop_admin, RegistrationStatus.PENDING);


            userRepository.save(user);
            userRepository.save(admin);
            userRepository.save(warehouse_admin);
            userRepository.save(warehouse_admin2);
            userRepository.save(shop_admin);
            userRepository.save(shop_admin2);

            WarehouseEntity warehouse = new WarehouseEntity("Warehouse Name", "Street 56", "12345",
                    warehouse_admin);
            WarehouseEntity warehouse2 = new WarehouseEntity("Firma Mea", "Street 57", "12385",
                    warehouse_admin2);
            warehouseRepository.save(warehouse);
            warehouseRepository.save(warehouse2);

            ShopEntity shop = new ShopEntity("Shop Name", "Strada 34", "12345", shop_admin);
            ShopEntity shop2 = new ShopEntity("Magazinul Meu", "Strada 4", "12395", shop_admin2);
            shopRepository.save(shop);
            shopRepository.save(shop2);

            ItemEntity item = new ItemEntity("Faina");
            ItemEntity item2 = new ItemEntity("Zahar");
            itemRepository.save(item);
            itemRepository.save(item2);

            ItemWarehouse itemWarehouse = new ItemWarehouse(item, warehouse, 100);
            ItemWarehouse itemWarehouse2 = new ItemWarehouse(item2, warehouse, 100);
            ItemWarehouse itemWarehouse3 = new ItemWarehouse(item2, warehouse2, 10);
            itemWarehouseRepository.save(itemWarehouse);
            itemWarehouseRepository.save(itemWarehouse2);
            itemWarehouseRepository.save(itemWarehouse3);
        };
    }
}
