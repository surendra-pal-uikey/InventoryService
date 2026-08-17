package org.sp.inventoryservice.services;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.sp.inventoryservice.clients.ProductClient;
import org.sp.inventoryservice.dto.InventoryRequestDto;
import org.sp.inventoryservice.dto.InventoryResponseDto;
import org.sp.inventoryservice.dto.ProductResponseDto;
import org.sp.inventoryservice.enums.InventoryStatus;
import org.sp.inventoryservice.exceptions.InventoryAlreadyExistsException;
import org.sp.inventoryservice.exceptions.InventoryNotFoundException;
import org.sp.inventoryservice.exceptions.ProductNotFoundException;
import org.sp.inventoryservice.mapper.InventoryMapper;
import org.sp.inventoryservice.models.Inventory;
import org.sp.inventoryservice.repositories.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductClient productClient;

    @Override
    @Transactional
    public InventoryResponseDto createInventory(InventoryRequestDto requestDto) {

        if (inventoryRepository.existsByProductId(requestDto.getProductId())) {
            throw new InventoryAlreadyExistsException(
                    "Inventory already exists for product id: " + requestDto.getProductId());
        }

        ProductResponseDto product = fetchProductOrThrow(requestDto.getProductId());

        Inventory inventory = InventoryMapper.toEntity(requestDto, product);
        Inventory saved = inventoryRepository.save(inventory);
        return InventoryMapper.toResponseDto(saved);
    }

    @Override
    public InventoryResponseDto getInventoryById(String inventoryId) {
        Inventory inventory = findInventoryOrThrow(inventoryId);
        return InventoryMapper.toResponseDto(inventory);
    }

    @Override
    public InventoryResponseDto getInventoryByProductId(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for product id: " + productId));
        return InventoryMapper.toResponseDto(inventory);
    }

    @Override
    public List<InventoryResponseDto> getAllInventory() {
        return inventoryRepository.findAll()
                .stream()
                .map(InventoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryResponseDto restockInventory(String inventoryId, Integer quantityToAdd) {
        Inventory inventory = findInventoryOrThrow(inventoryId);

        inventory.setQuantityAvailable(inventory.getQuantityAvailable() + quantityToAdd);
        inventory.setLastRestockedAt(LocalDateTime.now());
        updateStatus(inventory);

        Inventory updated = inventoryRepository.save(inventory);
        return InventoryMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public InventoryResponseDto reserveStock(String inventoryId, Integer quantityToReserve) {
        Inventory inventory = findInventoryOrThrow(inventoryId);

        int available = inventory.getQuantityAvailable() - inventory.getReservedQuantity();
        if (quantityToReserve > available) {
            throw new IllegalStateException(
                    "Insufficient stock. Available: " + available + ", requested: " + quantityToReserve);
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantityToReserve);
        updateStatus(inventory);

        Inventory updated = inventoryRepository.save(inventory);
        return InventoryMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteInventory(String inventoryId) {
        Inventory inventory = findInventoryOrThrow(inventoryId);
        inventoryRepository.delete(inventory);
    }

    // ---------- helpers ----------

    private Inventory findInventoryOrThrow(String inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found with id: " + inventoryId));
    }

    private ProductResponseDto fetchProductOrThrow(String productId) {
        try {
            return productClient.getProductById(productId);
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException(
                    "Cannot create inventory — product not found: " + productId);
        }
    }

    private void updateStatus(Inventory inventory) {
        int available = inventory.getQuantityAvailable() - inventory.getReservedQuantity();
        if (available <= 0) {
            inventory.setStatus(InventoryStatus.OUT_OF_STOCK);
        } else if (inventory.getReorderLevel() != null && available <= inventory.getReorderLevel()) {
            inventory.setStatus(InventoryStatus.LOW_STOCK);
        } else {
            inventory.setStatus(InventoryStatus.IN_STOCK);
        }
    }
}