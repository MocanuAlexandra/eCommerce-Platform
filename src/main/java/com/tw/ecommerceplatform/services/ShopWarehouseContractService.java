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

    public List<ShopWarehouseContract> getAllApprovedContracts(ShopEntity shop) {
        return shopWarehouseContractRepository.getAllByShopAndApprovedStatus(shop.getId());
    }

    public List<ShopWarehouseContract> getAllPendingContracts(WarehouseEntity warehouse) {
        return shopWarehouseContractRepository.getAllByWarehouseAndPendingStatus(warehouse.getWarehouse_id());
    }

    public List<ShopWarehouseContract> getAllNonExistingContracts(ShopEntity shop) {
        return shopWarehouseContractRepository.getAllByShopAndNonExistingStatus(shop.getId());
    }

    public ShopWarehouseContract getContractByShopAndWarehouse(ShopEntity shop, WarehouseEntity warehouse) {
        return shopWarehouseContractRepository.findByShopAndWarehouse(shop, warehouse);
    }

    public void saveContract(ShopEntity shop, WarehouseEntity warehouse) {
        ShopWarehouseContract shopWarehouseContract = getContractByShopAndWarehouse(shop, warehouse);
        shopWarehouseContract.setStatus(ContractStatus.PENDING);
        shopWarehouseContractRepository.save(shopWarehouseContract);
    }

    public void approveContract(Long id,WarehouseEntity warehouse) {
        ShopEntity shop = shopRepository.findById(id).get();
        ShopWarehouseContract shopWarehouseContract = getContractByShopAndWarehouse(shop, warehouse);
        shopWarehouseContract.setStatus(ContractStatus.APPROVED);
        shopWarehouseContractRepository.save(shopWarehouseContract);
    }

    public void rejectContract(Long id,WarehouseEntity warehouse) {
        ShopEntity shop = shopRepository.findById(id).get();
        ShopWarehouseContract shopWarehouseContract = getContractByShopAndWarehouse(shop, warehouse);
        shopWarehouseContract.setStatus(ContractStatus.NON_EXISTENT);
        shopWarehouseContractRepository.save(shopWarehouseContract);
    }
}
