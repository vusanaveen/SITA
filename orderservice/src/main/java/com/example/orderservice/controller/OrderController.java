package com.example.orderservice.controller;

import com.example.common.dto.ErrorResponse;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Order management operations.
 * 
 * This controller provides REST endpoints for order CRUD operations
 * with proper HTTP status codes and validation.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Management", description = "APIs for managing order operations")
public class OrderController {

    private final OrderService orderService;

    /**
     * Create a new order.
     * 
     * @param orderRequest the order data
     * @return the created order with 201 status
     */
    @PostMapping
    @Operation(summary = "1. Create order", description = "Creates a new order with the provided information. Validates user existence with UserService.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Quantity must be at least 1",
                      "status": 400,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("POST /orders - Creating order for user ID: {}", orderRequest.getUserId());
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * Get an order by ID.
     * 
     * @param id the order ID
     * @return the order with 200 status
     */
    @GetMapping("/{id}")
    @Operation(summary = "2. Get order by ID", description = "Retrieves an order by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found successfully",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Order not found with ID: 456",
                      "status": 404,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<OrderResponse> getOrderById(@Parameter(description = "Order ID") @PathVariable("id") Long id) {
        log.info("GET /orders/{} - Retrieving order", id);
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Get all orders.
     * 
     * @return list of all orders with 200 status
     */
    @GetMapping
    @Operation(summary = "Extra: List all orders", description = "Retrieves a list of all orders")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        log.info("GET /orders - Retrieving all orders");
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Get orders by user ID.
     * 
     * @param userId the user ID
     * @return list of orders for the user with 200 status
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Extra: Get orders by user ID", description = "Retrieves all orders for a specific user. Validates user existence with UserService.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "User not found with ID: 5",
                      "status": 400,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@Parameter(description = "User ID") @PathVariable("userId") Long userId) {
        log.info("GET /orders/user/{} - Retrieving orders for user", userId);
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Update an order by ID.
     * 
     * @param id the order ID
     * @param orderRequest the updated order data
     * @return the updated order with 200 status
     */
    @PutMapping("/{id}")
    @Operation(summary = "3. Update order by ID", description = "Updates an existing order with new information. Validates user existence with UserService.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order updated successfully",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Price must be positive",
                      "status": 400,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "User not found with ID: 5",
                      "status": 400,
                      "timestamp": "2024-01-01T00:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<OrderResponse> updateOrder(@Parameter(description = "Order ID") @PathVariable("id") Long id, 
                                                   @Valid @RequestBody OrderRequest orderRequest) {
        log.info("PUT /orders/{} - Updating order", id);
        OrderResponse updatedOrder = orderService.updateOrder(id, orderRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Delete an order by ID.
     * 
     * @param id the order ID
     * @return 204 No Content status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "4. Delete order by ID", description = "Deletes an order by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Order not found with ID: 456",
                      "status": 404,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "Order ID") @PathVariable("id") Long id) {
        log.info("DELETE /orders/{} - Deleting order", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
