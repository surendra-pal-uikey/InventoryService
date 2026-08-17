package org.sp.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequestDto {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotNull(message = "Quantity available is required")
    @Min(value = 0, message = "Quantity available cannot be negative")
    private Integer quantityAvailable;

    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;

    @Min(value = 0, message = "Reorder quantity cannot be negative")
    private Integer reorderQuantity;

    private String warehouseLocation;
}