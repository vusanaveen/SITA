package com.example.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for order creation and update requests.
 * 
 * This class represents the data transfer object used for
 * receiving order data from REST API requests.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order creation/update request")
public class OrderRequest {

    @Schema(description = "ID of the user who placed the order", example = "1")
    @NotNull(message = "User ID is required")
    private Long userId;

    @Schema(description = "Name of the product ordered", example = "Laptop")
    @NotBlank(message = "Product is required")
    private String product;

    @Schema(description = "Quantity of the product ordered", example = "2")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Schema(description = "Price of the product", example = "999.99")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
