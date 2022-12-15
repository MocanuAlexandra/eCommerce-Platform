package com.tw.ecommerceplatform.services;


import com.tw.ecommerceplatform.models.RoleEntity;
import com.tw.ecommerceplatform.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public List<RoleEntity> allRoles(){
        return roleRepository.findAll();
    }
}
