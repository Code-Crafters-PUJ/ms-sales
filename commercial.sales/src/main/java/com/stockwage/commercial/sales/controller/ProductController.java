package com.stockwage.commercial.sales.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.exception.AlreadyExistsException;
import com.stockwage.commercial.sales.service.product.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@Api(tags = "Product Management", description = "Endpoints for managing products")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @GetMapping("/all")
    @Operation(summary = "Get all products", description = "Retrieves a list of all products")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @ApiResponse(responseCode = "404", description = "No products found")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAll();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new product", description = "Adds a new product")
    @ApiResponse(responseCode = "201", description = "Product added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "409", description = "Product already exists")
    public ResponseEntity<?> addNewProduct(@RequestBody Product product) {
        try {
            Product newProduct = productService.save(product);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>("El producto ya existe", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a product", description = "Updates an existing product")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Optional<Product> existingProduct = productService.getById(id);
        if (existingProduct.isPresent()) {
            product.setId(id);
            Product updatedProduct = productService.update(product);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (productService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
