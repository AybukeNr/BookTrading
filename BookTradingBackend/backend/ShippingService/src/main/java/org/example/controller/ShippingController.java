package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateShippingRequest;
import org.example.dto.request.UpdateShippingRequest;
import org.example.dto.response.ExchangeResponse;
import org.example.dto.response.ShippingResponse;
import org.example.service.impl.ShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.constant.RestApiList.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(SHIPPINGS)
@Tag(name = "Shipping Controller", description = "Operations related to shipping and exchanges")
public class ShippingController {

    private final ShippingService shippingService;

    @Operation(summary = "Get user shippings", description = "Retrieves all shippings for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shippings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(GET_USERS_SHIPPINGS)
    public ResponseEntity<List<ShippingResponse>> getUsersShippings(@RequestParam String userId) {
        return ResponseEntity.ok(shippingService.getUsersShippings(userId));
    }

    @Operation(summary = "Create a new shipping,servisler arası endpoint", description = "Creates a new shipping record based on the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    //servisler arası endpoint
    @PostMapping(CREATE_SHIPPING)
    public ResponseEntity<Boolean> createShipping(@RequestBody CreateShippingRequest createShippingRequest) {
        return ResponseEntity.ok(shippingService.createShipping(createShippingRequest));
    }

    @Operation(summary = "Update shipping status", description = "Updates the status of an existing shipping.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Shipping not found")
    })
    @PatchMapping(UPDATE_SHIPPING)
    public ResponseEntity<ShippingResponse> updateShippingStatus(@RequestBody UpdateShippingRequest updateShippingRequest) {
        return ResponseEntity.ok(shippingService.updateShippingStatus(updateShippingRequest));
    }

    @Operation(summary = "Cancel an exchange", description = "Cancels an exchange based on the transaction ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchange cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Exchange not found")
    })
    @PatchMapping(CANCEL_EXCHANGE)
    public ResponseEntity<ExchangeResponse> cancelExchangeStatus(@RequestParam String transactionId) {
        return ResponseEntity.ok(shippingService.cancelExchangeStatus(transactionId));
    }

    @Operation(summary = "Set shipping as delivered", description = "Marks a shipping as delivered using the serial number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping marked as delivered successfully"),
            @ApiResponse(responseCode = "404", description = "Shipping not found")
    })
    @PatchMapping(SET_DELIVERED)
    public ResponseEntity<ShippingResponse> setDelivered(@RequestParam String shippingSerialNumber) {
        return ResponseEntity.ok(shippingService.delivered(shippingSerialNumber));
    }

    @Operation(summary = "Get all shippings", description = "Retrieves all shipping records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shippings retrieved successfully")
    })
    @GetMapping(GET_ALL_SHIPPINGS)
    public ResponseEntity<List<ShippingResponse>> getAllShippings() {
        return ResponseEntity.ok(shippingService.getAllShippings());
    }

    @Operation(summary = "Get shipping by serial number", description = "Retrieves a shipping record using its serial number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shipping not found")
    })
    @GetMapping(GET_SHIPPING_BY_SN)
    public ResponseEntity<ShippingResponse> getShippingBySerialNumber(@RequestParam String serialNumber) {
        return ResponseEntity.ok(shippingService.getShippingBySn(serialNumber));
    }

    @Operation(summary = "Get all exchanges", description = "Retrieves all exchange records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchanges retrieved successfully")
    })
    @GetMapping(GET_ALL_EXCHANGES)
    public ResponseEntity<List<ExchangeResponse>> getAllExchanges() {
        return ResponseEntity.ok(shippingService.getAllExchanges());
    }

    @Operation(summary = "Get exchanges by status", description = "Retrieves exchanges filtered by their status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchanges retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Exchanges not found")
    })
    @GetMapping(GET_ALL_EXS_BY_STATUS)
    public ResponseEntity<List<ExchangeResponse>> getExchangesByStatus(@RequestParam String status) {
        return ResponseEntity.ok(shippingService.getAllExchangesByStatus(status));
    }

    @Operation(summary = "Get exchange by transaction ID", description = "Retrieves an exchange record using its transaction ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchange retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Exchange not found")
    })
    @GetMapping(GET_EXCHANGE_BY_TID)
    public ResponseEntity<ExchangeResponse> getExchangeByTransactionId(@RequestParam String transactionId) {
        return ResponseEntity.ok(shippingService.getExchangeByTID(transactionId));
    }

    @Operation(summary = "Get exchanges by user ID", description = "Retrieves all exchanges related to a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchanges retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(GET_USERS_EXCHANGE)
    public ResponseEntity<List<ExchangeResponse>> getExchangesByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(shippingService.getExchangeByUserID(userId));
    }
}