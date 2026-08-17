package org.sp.inventoryservice.mapper;

import org.sp.inventoryservice.dto.InventoryRequestDto;
import org.sp.inventoryservice.dto.InventoryResponseDto;
import org.sp.inventoryservice.dto.ProductResponseDto;
import org.sp.inventoryservice.models.Inventory;

public class InventoryMapper {

    public static Inventory toEntity(InventoryRequestDto dto, ProductResponseDto product) {
        return Inventory.builder()
                .productId(product.getProductId())
                .sku(product.getSku())
                .quantityAvailable(dto.getQuantityAvailable())
                .reorderLevel(dto.getReorderLevel())
                .reorderQuantity(dto.getReorderQuantity())
                .warehouseLocation(dto.getWarehouseLocation())
                .build();
    }

    public static InventoryResponseDto toResponseDto(Inventory inventory) {
        return InventoryResponseDto.builder()
                .inventoryId(inventory.getInventoryId())
                .productId(inventory.getProductId())
                .sku(inventory.getSku())
                .quantityAvailable(inventory.getQuantityAvailable())
                .reservedQuantity(inventory.getReservedQuantity())
                .availableForSale(inventory.getAvailableForSale())
                .reorderLevel(inventory.getReorderLevel())
                .reorderQuantity(inventory.getReorderQuantity())
                .warehouseLocation(inventory.getWarehouseLocation())
                .status(inventory.getStatus() != null ? inventory.getStatus().name() : null)
                .lastRestockedAt(inventory.getLastRestockedAt())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
}
