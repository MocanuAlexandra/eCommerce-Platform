package com.tw.ecommerceplatform.services;


import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleEntity getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
