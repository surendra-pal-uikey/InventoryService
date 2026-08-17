package org.sp.inventoryservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sp.inventoryservice.dto.InventoryRequestDto;
import org.sp.inventoryservice.dto.InventoryResponseDto;
import org.sp.inventoryservice.services.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponseDto> createInventory(
            @Valid @RequestBody InventoryRequestDto requestDto) {
        InventoryResponseDto response = inventoryService.createInventory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponseDto> getInventoryById(
            @PathVariable String inventoryId) {
        return ResponseEntity.ok(inventoryService.getInventoryById(inventoryId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponseDto> getInventoryByProductId(
            @PathVariable String productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDto>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @PostMapping("/{inventoryId}/restock")
    public ResponseEntity<InventoryResponseDto> restockInventory(
            @PathVariable String inventoryId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.restockInventory(inventoryId, quantity));
    }

    @PostMapping("/{inventoryId}/reserve")
    public ResponseEntity<InventoryResponseDto> reserveStock(
            @PathVariable String inventoryId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.reserveStock(inventoryId, quantity));
    }

    @PostMapping("/product/{productId}/reserve")
    public ResponseEntity<InventoryResponseDto> reserveStockByProductId(
            @PathVariable String productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.reserveStockByProductId(productId, quantity));
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable String inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/product/{productId}/deduct")
    public ResponseEntity<InventoryResponseDto> deductStockByProductId(
            @PathVariable String productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.deductStockByProductId(productId, quantity));
    }

    @PostMapping("/product/{productId}/release")
    public ResponseEntity<InventoryResponseDto> releaseStockByProductId(
            @PathVariable String productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.releaseStockByProductId(productId, quantity));
    }
}