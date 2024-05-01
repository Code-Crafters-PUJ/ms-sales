package com.stockwage.commercial.sales.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockwage.commercial.sales.dto.PaymentMethodDTO;
import com.stockwage.commercial.sales.entity.PaymentMethod;
import com.stockwage.commercial.sales.service.paymentmethod.PaymentMethodService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paymentMethod")
@Api(tags = "Payment Method Management", description = "Endpoints for managing payment methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/all")
    @Operation(summary = "Get all payment methods", description = "Retrieves a list of all payment methods")
    @ApiResponse(responseCode = "200", description = "Payment methods retrieved successfully")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodService.getAll();
        return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new payment method", description = "Adds a new payment method")
    @ApiResponse(responseCode = "201", description = "Payment method added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "409", description = "Payment method already exists")
    public ResponseEntity<PaymentMethod> addPaymentMethod(@RequestBody PaymentMethodDTO paymentMethod) {
        if (paymentMethod == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            PaymentMethod newPaymentMethod = paymentMethodService.save(paymentMethodService.DtoToEntity(paymentMethod));
            return new ResponseEntity<>(newPaymentMethod, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get a payment method by ID", description = "Retrieves a payment method by its ID")
    @ApiResponse(responseCode = "200", description = "Payment method retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<PaymentMethod> paymentMethod = paymentMethodService.getById(id);
        return paymentMethod.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update an existing payment method", description = "Updates an existing payment method")
    @ApiResponse(responseCode = "200", description = "Payment method updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied or Bad request")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentMethodDTO updatedPaymentMethod) {
        if (id == null || updatedPaymentMethod == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<PaymentMethod> existingPaymentMethodOptional = paymentMethodService.getById(id);
        if (existingPaymentMethodOptional.isPresent()) {
            PaymentMethod existingPaymentMethod = existingPaymentMethodOptional.get();
            existingPaymentMethod = paymentMethodService.DtoToEntity(updatedPaymentMethod);
            existingPaymentMethod.setId(id);
            try {
                PaymentMethod savedPaymentMethod = paymentMethodService.update(existingPaymentMethod);
                return new ResponseEntity<>(savedPaymentMethod, HttpStatus.OK);
            } catch (DuplicateKeyException e) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a payment method by ID", description = "Deletes a payment method by its ID")
    @ApiResponse(responseCode = "204", description = "Payment method deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean deleted = paymentMethodService.delete(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
