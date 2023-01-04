package com.tw.ecommerceplatform;

import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.entities.UserEntity;
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

    @Bean
    public CommandLineRunner insert(RoleRepository roleRepository,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {

            // Create roles
            RoleEntity role_user = new RoleEntity(Role.CUSTOMER.getName());
            RoleEntity role_admin = new RoleEntity(Role.ADMIN.getName());
            RoleEntity role_warehouse_admin = new RoleEntity(Role.WAREHOUSE_ADMIN.getName());
            RoleEntity role_shop_admin = new RoleEntity(Role.SHOP_ADMIN.getName());
            roleRepository.save(role_user);
            roleRepository.save(role_admin);
            roleRepository.save(role_warehouse_admin);
            roleRepository.save(role_shop_admin);

            // Create admin
            UserEntity admin = new UserEntity("admin@gmail.com",
                    passwordEncoder.encode("admin"), role_admin, RegistrationStatus.APPROVED);
            userRepository.save(admin);
        };
    }
}
