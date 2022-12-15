package com.example.ecommerceplatform.services;


import com.example.ecommerceplatform.models.RoleEntity;
import com.example.ecommerceplatform.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public List<RoleEntity> allRoles(){
        return (List<RoleEntity>) roleRepository.findAll();
    }
}
