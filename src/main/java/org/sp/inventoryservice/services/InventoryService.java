package org.sp.inventoryservice.services;

import org.sp.inventoryservice.dto.InventoryRequestDto;
import org.sp.inventoryservice.dto.InventoryResponseDto;

import java.util.List;

public interface InventoryService {

    InventoryResponseDto createInventory(InventoryRequestDto requestDto);

    InventoryResponseDto getInventoryById(String inventoryId);

    InventoryResponseDto getInventoryByProductId(String productId);

    List<InventoryResponseDto> getAllInventory();

    InventoryResponseDto restockInventory(String inventoryId, Integer quantityToAdd);

    InventoryResponseDto reserveStock(String inventoryId, Integer quantityToReserve);

    InventoryResponseDto reserveStockByProductId(String productId, Integer quantityToReserve);

    void deleteInventory(String inventoryId);

    InventoryResponseDto deductStockByProductId(String productId, Integer quantity);

    InventoryResponseDto releaseStockByProductId(String productId, Integer quantity);
}