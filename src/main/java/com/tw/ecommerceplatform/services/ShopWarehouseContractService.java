package com.tw.ecommerceplatform.services;

import com.tw.ecommerceplatform.entities.ShopEntity;
import com.tw.ecommerceplatform.entities.ShopWarehouseContract;
import com.tw.ecommerceplatform.entities.WarehouseEntity;
import com.tw.ecommerceplatform.repositories.ShopRepository;
import com.tw.ecommerceplatform.repositories.ShopWarehouseContractRepository;
import com.tw.ecommerceplatform.utility.ContractStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopWarehouseContractService {
    private final ShopWarehouseContractRepository shopWarehouseContractRepository;
    private final WarehouseService warehouseService;
    private final ShopService shopService;
    private final ShopRepository shopRepository;

    // Add contract with 'NON-EXISTENT' status between shop and all of warehouses
    public void addNonExistingContractsShopWarehouses(Long shopId) {

        List<WarehouseEntity> warehouses = warehouseService.getAllApprovedWarehouses();

        for (WarehouseEntity warehouse : warehouses) {
            ShopWarehouseContract shopWarehouseContract = new ShopWarehouseContract();
            shopWarehouseContract.setShop(shopService.getShopById(shopId));
            shopWarehouseContract.setStatus(ContractStatus.NON_EXISTENT);
            shopWarehouseContract.setWarehouse(warehouse);
            shopWarehouseContractRepository.save(shopWarehouseContract);
        }
    }

    // Add contract with 'NON-EXISTENT' status between warehouse and all of shops
    public void addNonExistingContractsWarehouseShops(Long warehouseId) {

        List<ShopEntity> shops = shopService.getAllApprovedShops();

        for (ShopEntity shop : shops) {
            ShopWarehouseContract shopWarehouseContract = new ShopWarehouseContract();
            shopWarehouseContract.setWarehouse(warehouseService.getWarehouseById(warehouseId));
            shopWarehouseContract.setStatus(ContractStatus.NON_EXISTENT);
            shopWarehouseContract.setShop(shop);
            shopWarehouseContractRepository.save(shopWarehouseContract);
        }
    }

    // Get all approved contracts of a shop
    public List<ShopWarehouseContract> getAllApprovedContracts(ShopEntity shop) {
        return shopWarehouseContractRepository.findAllByShopAndApprovedStatus(shop.getId());
    }

    // Get all pending contracts of a shop
    public List<ShopWarehouseContract> getAllPendingContracts(WarehouseEntity warehouse) {
        return shopWarehouseContractRepository.findAllByWarehouseAndPendingStatus(warehouse.getWarehouse_id());
    }

    // Get all non-existing contracts
    public List<ShopWarehouseContract> getAllNonExistingContracts(ShopEntity shop) {
        return shopWarehouseContractRepository.findAllByShopAndNonExistingStatus(shop.getId());
    }

    // Get contract by shop and warehouse
    public ShopWarehouseContract getContractByShopAndWarehouse(ShopEntity shop, WarehouseEntity warehouse) {
        return shopWarehouseContractRepository.findByShopAndWarehouse(shop, warehouse);
    }

    // Save contract into database with `PENDING` status
    public void saveContract(ShopEntity shop, WarehouseEntity warehouse) {
        ShopWarehouseContract shopWarehouseContract = getContractByShopAndWarehouse(shop, warehouse);
        shopWarehouseContract.setStatus(ContractStatus.PENDING);
        shopWarehouseContractRepository.save(shopWarehouseContract);
    }

    // Approve contract
    public void approveContract(Long id, WarehouseEntity warehouse) {
        ShopEntity shop = shopRepository.findById(id).get();
        ShopWarehouseContract shopWarehouseContract = getContractByShopAndWarehouse(shop, warehouse);
        shopWarehouseContract.setStatus(ContractStatus.APPROVED);
        shopWarehouseContractRepository.save(shopWarehouseContract);
    }

    // Reject contract
    public void rejectContract(Long id, WarehouseEntity warehouse) {
        ShopEntity shop = shopRepository.findById(id).get();
        ShopWarehouseContract shopWarehouseContract = getContractByShopAndWarehouse(shop, warehouse);
        shopWarehouseContract.setStatus(ContractStatus.NON_EXISTENT);
        shopWarehouseContractRepository.save(shopWarehouseContract);
    }
}
