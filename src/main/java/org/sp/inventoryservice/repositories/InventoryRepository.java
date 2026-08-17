package org.sp.inventoryservice.repositories;

import org.sp.inventoryservice.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, String> {

    Optional<Inventory> findByProductId(String productId);

    boolean existsByProductId(String productId);

    List<Inventory> findByWarehouseLocation(String warehouseLocation);
}
