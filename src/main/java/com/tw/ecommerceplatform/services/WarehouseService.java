package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.models.RegisterUserModel;
import com.tw.ecommerceplatform.models.RegisterWarehouseShopModel;
import com.tw.ecommerceplatform.repositories.WarehouseRepository;
import com.tw.ecommerceplatform.utility.RegistrationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final JpaUserDetailsService userDetailsService;
    private final WarehouseRepository warehouseRepository;

    // Register Warehouse
    public void registerWarehouse(RegisterWarehouseShopModel registerWarehouseShopModel, RoleEntity role) throws Exception {

        // Get user details
        RegisterUserModel registerUserModel = new RegisterUserModel(
                registerWarehouseShopModel.getUsername(),
                registerWarehouseShopModel.getPassword(),
                registerWarehouseShopModel.getConfirmPassword());

        // Register user
        UserEntity warehouseAdmin = userDetailsService.registerUser(registerUserModel, role);

        // Check if the warehouse already exists
        WarehouseEntity savedWarehouse = warehouseRepository.findByName(registerWarehouseShopModel.getName());
        if (savedWarehouse != null) {
            throw new Exception("Warehouse already exists");
        } else {

            // Create warehouse
            WarehouseEntity newWarehouse = new WarehouseEntity(
                    registerWarehouseShopModel.getName(),
                    registerWarehouseShopModel.getAddress(),
                    registerWarehouseShopModel.getCode(),
                    warehouseAdmin);

            // Save warehouse into database
            warehouseRepository.save(newWarehouse);
        }
    }

    // Get warehouse by id
    public WarehouseEntity getWarehouseById(Long warehouseId) {
        return warehouseRepository.findById(warehouseId).get();
    }

    // Get warehouse by user's email
    public WarehouseEntity getWarehouseByAdminEmail(String email) {
        return warehouseRepository.findByAdminWarehouse_Email(email);
    }

    // Save warehouse into database
    public void saveWarehouse(WarehouseEntity warehouse) {
        warehouseRepository.save(warehouse);
    }

    // Get all warehouses that are in pending status
    public List<WarehouseEntity> getAllPendingWarehouses() {
        return warehouseRepository.getAllPendingWarehouses();
    }

    // Get all warehouses that are in approved status
    public List<WarehouseEntity> getAllApprovedWarehouses() {
        return warehouseRepository.getAllApprovedWarehouses();
    }

    // Delete warehouse
    public void deleteWarehouse(Long id) {
        warehouseRepository.deleteById(id);
    }

    // Function used to reject a warehouse
    public void rejectWarehouse(Long id) {
        Long userId = getWarehouseById(id).getAdminWarehouse().getId();
        deleteWarehouse(id);
        userDetailsService.deleteUser(userId);
    }

    // Function used to approve a warehouse
    public void approveRegistration(Long id) {
        WarehouseEntity warehouse = getWarehouseById(id);
        warehouse.getAdminWarehouse().setStatus(RegistrationStatus.APPROVED);
        saveWarehouse(warehouse);
    }
}
