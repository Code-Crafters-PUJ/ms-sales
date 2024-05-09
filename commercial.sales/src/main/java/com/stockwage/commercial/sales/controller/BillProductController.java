package com.stockwage.commercial.sales.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.dto.BillProductDTO;
import com.stockwage.commercial.sales.entity.BillProduct;
import com.stockwage.commercial.sales.service.bill.BillService;
import com.stockwage.commercial.sales.service.billproduct.BillProductService;
import com.stockwage.commercial.sales.service.product.ProductService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/billproduct")
@Api(tags = "Bill's Products Management", description = "Endpoints for managing bill's products")
@PreAuthorize("hasRole('Raiz') or hasRole('Ventas')")
public class BillProductController {

    @Autowired
    private BillProductService billProductService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BillService billService;

    @GetMapping("/all")
    @Operation(summary = "Get all products of the bills", description = "Retrieves a list of all bills's products")
    @ApiResponse(responseCode = "200", description = "Bills retrieved successfully")
    public ResponseEntity<List<BillProductDTO>> getAllBills() {
        List<BillProductDTO> bills = billProductService.getAll();
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @GetMapping("/allByBill/{billId}")
    @Operation(summary = "Get all products of the bills by bill", description = "Retrieves a list of all products by bill")
    @ApiResponse(responseCode = "200", description = "Bills retrieved successfully")
    @ApiResponse(responseCode = "404", description = "No bill found")
    public ResponseEntity<List<BillProductDTO>> getAllProductsByBill(@PathVariable Long billId) {
        Optional<BillDTO> existingBillOptional = billService.getById(billId);
        if (!existingBillOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        System.out.println("Aqui estamos");
        List<BillProductDTO> bills = billProductService.getAllByBill(billId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @PostMapping("/addToBill/{billId}")
    @Transactional
    @Operation(summary = "Add a new product to the bill", description = "Adds a new product to the bill")
    @ApiResponse(responseCode = "201", description = "Product added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<?> addBill(@RequestBody BillProductDTO billProductDTO, @PathVariable Long billId) {
        BillProduct newBillProduct = billProductService.save(billProductDTO, billId);
        if (newBillProduct == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        productService.updateProductQuantity(billProductDTO.getProduct_id(), billProductDTO.getQuantity());
        return new ResponseEntity<>(newBillProduct, HttpStatus.CREATED);
    }

    @PostMapping("/addProductsToBill/{billId}")
    @Transactional
    @Operation(summary = "Add products to a bill", description = "Adds multiple products to a bill")
    @ApiResponse(responseCode = "201", description = "Products added to bill successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<?> addProductsToBill(@RequestBody List<BillProductDTO> billProducts, @PathVariable Long billId) {
        if (billProducts == null || billProducts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (BillProductDTO billProductDTO : billProducts) {
            if (billId == null || billProductDTO.getProduct_id() == null || billProductDTO.getQuantity() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            BillProduct newBillProduct = billProductService.save(billProductDTO, billId);
            if (newBillProduct == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            productService.updateProductQuantity(billProductDTO.getProduct_id(), billProductDTO.getQuantity());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    
    
}

