package com.stockwage.commercial.sales.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockwage.commercial.sales.dto.BillProductDTO;
import com.stockwage.commercial.sales.entity.BillProduct;
import com.stockwage.commercial.sales.service.billproduct.BillProductService;
import com.stockwage.commercial.sales.service.product.ProductService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/billproduct")
@Api(tags = "Bill's Products Management", description = "Endpoints for managing bill's products")
public class BillProductController {

    @Autowired
    private BillProductService billProductService;

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    @Operation(summary = "Get all products of the bills", description = "Retrieves a list of all bills's products")
    @ApiResponse(responseCode = "200", description = "Bills retrieved successfully")
    @ApiResponse(responseCode = "404", description = "No bills found")
    public ResponseEntity<List<BillProductDTO>> getAllBills() {
        List<BillProductDTO> bills = billProductService.getAll();
        if (bills.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @GetMapping("/allByBill")
    @Operation(summary = "Get all products of the bills by bill", description = "Retrieves a list of all products by bill")
    @ApiResponse(responseCode = "200", description = "Bills retrieved successfully")
    @ApiResponse(responseCode = "404", description = "No bills found")
    public ResponseEntity<List<BillProductDTO>> getAllBillsByBill(@RequestParam Long billId) {
        List<BillProductDTO> bills = billProductService.getAllByBill(billId);
        if (bills.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @PostMapping("/addToBill")
    @Transactional
    @Operation(summary = "Add a new product to the bill", description = "Adds a new product to the bill")
    @ApiResponse(responseCode = "201", description = "Product added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<?> addBill(@RequestBody BillProductDTO billProductDTO) {
        BillProduct newBillProduct = billProductService.save(billProductDTO);
        if (newBillProduct == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        productService.updateProductQuantity(billProductDTO.getProduct_id(), billProductDTO.getQuantity());
        return new ResponseEntity<>(newBillProduct, HttpStatus.CREATED);
    }

    
    
}
