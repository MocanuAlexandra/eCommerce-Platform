package com.tw.ecommerceplatform.repositories;

import com.tw.ecommerceplatform.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    void deleteById(Long id);
}
