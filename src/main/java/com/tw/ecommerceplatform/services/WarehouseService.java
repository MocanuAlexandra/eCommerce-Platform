package com.tw.ecommerceplatform.services;

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

    // Register Warehouse
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
        if (savedWarehouse != null) {
            throw new Exception("Warehouse already exists");
        } else {

            // Create warehouse
            WarehouseEntity warehouseEntity = new WarehouseEntity(
                    registerWarehouseModel.getName(),
                    registerWarehouseModel.getAddress(),
                    registerWarehouseModel.getCode(),
                    warehouseAdmin);

            // Save warehouse into database
            warehouseRepository.save(warehouseEntity);
        }
    }

    public WarehouseEntity getWarehouseById(Long warehouseId) {
        return warehouseRepository.findById(warehouseId).get();
    }

    public void save(WarehouseEntity warehouse) {
        warehouseRepository.save(warehouse);
    }

    public List<WarehouseEntity> getAllPendingWarehouses() {
        return warehouseRepository.getAllPendingWarehouses();
    }

    public void deleteWarehouse(Long id)
    {
        warehouseRepository.deleteById(id);
    }
}
