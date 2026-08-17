package org.sp.inventoryservice.models;

import org.sp.inventoryservice.enums.InventoryStatus;
import org.sp.inventoryservice.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @Column(name = "inventory_id", updatable = false, nullable = false, unique = true, length = 19)
    private String inventoryId;

    @Column(name = "product_id", nullable = false, unique = true, length = 19)
    private String productId;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "reorder_quantity")
    private Integer reorderQuantity;

    @Column(name = "warehouse_location")
    private String warehouseLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InventoryStatus status;

    @Column(name = "last_restocked_at")
    private LocalDateTime lastRestockedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void generateInventoryId() {
        if (this.inventoryId == null) {
            this.inventoryId = IdGenerator.generate("INV", 16);
        }
        if (this.reservedQuantity == null) {
            this.reservedQuantity = 0;
        }
        if (this.status == null) {
            this.status = InventoryStatus.IN_STOCK;
        }
    }

    @Transient
    public Integer getAvailableForSale() {
        return quantityAvailable - reservedQuantity;
    }
}