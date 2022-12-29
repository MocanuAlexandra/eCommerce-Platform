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
    private final ShopRepository shopRepository;
    private final WarehouseService warehouseService;

    // Add contract with 'NON-EXISTENT' status between shop and all of warehouses
    public void addNonExistingContracts(Long shopId) {

        List<WarehouseEntity> warehouses = warehouseService.getAllApprovedWarehouses();
        ShopWarehouseContract shopWarehouseContract = new ShopWarehouseContract();
        shopWarehouseContract.setShop(shopRepository.findById(shopId).get());
        shopWarehouseContract.setStatus(ContractStatus.NON_EXISTENT);

        for (WarehouseEntity warehouse : warehouses) {
            shopWarehouseContract.setWarehouse(warehouse);
            shopWarehouseContractRepository.save(shopWarehouseContract);
        }
    }

    // Get all approved contracts of a shop
    public List<ShopWarehouseContract> getAllApprovedContracts(ShopEntity shop) {
        return shopWarehouseContractRepository.getAllByShopAndApprovedStatus(shop.getId());
    }

    // Get all pending contracts of a shop
    public List<ShopWarehouseContract> getAllPendingContracts(WarehouseEntity warehouse) {
        return shopWarehouseContractRepository.getAllByWarehouseAndPendingStatus(warehouse.getWarehouse_id());
    }

    // Get all non-existing contracts
    public List<ShopWarehouseContract> getAllNonExistingContracts(ShopEntity shop) {
        return shopWarehouseContractRepository.getAllByShopAndNonExistingStatus(shop.getId());
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
