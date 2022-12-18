package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.Utility.RegistrationStatus;
import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.models.RegisterUserModel;
import com.tw.ecommerceplatform.models.RegisterWarehouseModel;
import com.tw.ecommerceplatform.repositories.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final JpaUserDetailsService userDetailsService;
    private final WarehouseRepository warehouseRepository;

    public void registerWarehouse(RegisterWarehouseModel registerWarehouseModel, RoleEntity role) throws Exception {

        // Get user details
        RegisterUserModel registerUserModel = new RegisterUserModel(
                registerWarehouseModel.getUsername(),
                registerWarehouseModel.getPassword(),
                registerWarehouseModel.getConfirmPassword());

        // Register user
        UserEntity warehouseAdmin = userDetailsService.registerUser(registerUserModel, role);

        // Check if the warehouse already exists
        WarehouseEntity savedWarehouse = warehouseRepository.findByName(registerWarehouseModel.getName());
        if (savedWarehouse != null && savedWarehouse.getStatus() == RegistrationStatus.APPROVED) {
            throw new Exception("Warehouse already exists");
        } else if (savedWarehouse != null && savedWarehouse.getStatus() == RegistrationStatus.PENDING) {
            throw new Exception("Warehouse already exists and is pending approval");
        } else {
            // Create warehouse with status pending and assign admin
            WarehouseEntity warehouseEntity = new WarehouseEntity(
                    registerWarehouseModel.getName(),
                    registerWarehouseModel.getAddress(),
                    registerWarehouseModel.getCode(),
                    warehouseAdmin,
                    RegistrationStatus.PENDING);

            // Save warehouse into database
            warehouseRepository.save(warehouseEntity);
        }
    }

    public List<WarehouseEntity> getAllPendingWarehouses() {
        return warehouseRepository.getAllPendingWarehouses();
    }
}
