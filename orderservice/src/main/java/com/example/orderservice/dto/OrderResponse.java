package com.example.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for order response data.
 * 
 * This class represents the data transfer object used for
 * returning order data in REST API responses.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order response data")
public class OrderResponse {

    @Schema(description = "Unique identifier of the order", example = "1")
    private Long id;
    
    @Schema(description = "ID of the user who placed the order", example = "1")
    private Long userId;
    
    @Schema(description = "Name of the product ordered", example = "Laptop")
    private String product;
    
    @Schema(description = "Quantity of the product ordered", example = "2")
    private Integer quantity;
    
    @Schema(description = "Price of the product", example = "999.99")
    private BigDecimal price;
}
