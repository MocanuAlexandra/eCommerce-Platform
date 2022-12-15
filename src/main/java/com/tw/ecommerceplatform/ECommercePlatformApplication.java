package com.tw.ecommerceplatform;

import com.tw.ecommerceplatform.models.RoleEntity;
import com.tw.ecommerceplatform.models.UserEntity;
import com.tw.ecommerceplatform.repositories.RoleRepository;
import com.tw.ecommerceplatform.repositories.UserRepository;
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
	public CommandLineRunner insert(RoleRepository roleRepository, UserRepository userRepository,
									PasswordEncoder passwordEncoder){
		return args -> {
			RoleEntity role_user = new RoleEntity("ROLE_USER");
			RoleEntity role_admin = new RoleEntity("ROLE_ADMIN");

			roleRepository.save(role_user);
			roleRepository.save(role_admin);


			UserEntity user = new UserEntity("username",
					passwordEncoder.encode("password"), role_user);
			UserEntity admin = new UserEntity("admin",
					passwordEncoder.encode("admin"), role_admin);

			userRepository.save(user);
			userRepository.save(admin);
		};
	}
}
