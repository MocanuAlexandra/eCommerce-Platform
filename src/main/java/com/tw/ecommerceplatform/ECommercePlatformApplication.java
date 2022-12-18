package com.tw.ecommerceplatform;

import com.tw.ecommerceplatform.Utility.RegistrationStatus;
import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.repositories.RoleRepository;
import com.tw.ecommerceplatform.repositories.UserRepository;
import com.tw.ecommerceplatform.repositories.WarehouseRepository;
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
									PasswordEncoder passwordEncoder) {
		return args -> {
			RoleEntity role_user = new RoleEntity("ROLE_CUSTOMER");
			RoleEntity role_admin = new RoleEntity("ROLE_ADMIN");
			RoleEntity role_warehouse_admin = new RoleEntity("ROLE_WAREHOUSE_ADMIN");

			roleRepository.save(role_user);
			roleRepository.save(role_admin);
			roleRepository.save(role_warehouse_admin);


			UserEntity user = new UserEntity("username@gmail.com",
					passwordEncoder.encode("password"), role_user);
			UserEntity admin = new UserEntity("admin@gmail.com",
					passwordEncoder.encode("admin"), role_admin);
			UserEntity warehouse_admin = new UserEntity("warehouse@gmail.com",
					passwordEncoder.encode("admin"), role_warehouse_admin);

			userRepository.save(user);
			userRepository.save(admin);
			userRepository.save(warehouse_admin);

			WarehouseEntity warehouse = new WarehouseEntity("Warehouse Name", "Street 56", "12345",
					warehouse_admin, RegistrationStatus.APPROVED);
			warehouseRepository.save(warehouse);

		};
	}
}
