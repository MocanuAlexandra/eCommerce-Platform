package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.entities.ShopEntity;
import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.models.RegisterUserModel;
import com.tw.ecommerceplatform.models.RegisterWarehouseShopModel;
import com.tw.ecommerceplatform.repositories.ShopRepository;
import com.tw.ecommerceplatform.utility.RegistrationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final JpaUserDetailsService userDetailsService;
    private final ShopRepository shopRepository;

    // Register Shop
    public void registerShop(RegisterWarehouseShopModel registerWarehouseShopModel, RoleEntity role) throws Exception {

        // Get user details
        RegisterUserModel registerUserModel = new RegisterUserModel(
                registerWarehouseShopModel.getUsername(),
                registerWarehouseShopModel.getPassword(),
                registerWarehouseShopModel.getConfirmPassword());

        // Register user
        UserEntity shopAdmin = userDetailsService.registerUser(registerUserModel, role);

        // Check if the shop already exists
        ShopEntity savedShop = shopRepository.findByName(registerWarehouseShopModel.getName());
        if (savedShop != null) {
            throw new Exception("Shop already exists");
        } else {

            // Create shop
            ShopEntity newShop = new ShopEntity(
                    registerWarehouseShopModel.getName(),
                    registerWarehouseShopModel.getAddress(),
                    registerWarehouseShopModel.getCode(),
                    shopAdmin);

            // Save shop into database
            shopRepository.save(newShop);
        }
    }

    // Get shop by id
    public ShopEntity getShopById(Long shopId) {
        return shopRepository.findById(shopId).get();
    }

    // Save shop into database
    public void saveShop(ShopEntity shop) {
        shopRepository.save(shop);
    }

    // Get all shops that are in pending status
    public List<ShopEntity> getAllPendingShops() {
        return shopRepository.getAllPendingShops();
    }

    // Delete shop
    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }

    // Function used to reject a registration for a shop
    public void rejectRegistration(Long id) {
        Long userId = getShopById(id).getAdminShop().getId();
        deleteShop(id);
        userDetailsService.deleteUser(userId);
    }

    // Function used to approve a registration for a shop
    public void approveShop(Long id) {
        ShopEntity shop = getShopById(id);
        shop.getAdminShop().setStatus(RegistrationStatus.APPROVED);
        saveShop(shop);
    }

    // Get shop by user's email
    public ShopEntity getShopByAdminEmail(String username) {
        return shopRepository.findByAdminShop_Email(username);
    }
}
