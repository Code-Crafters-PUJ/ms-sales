package com.stockwage.commercial.sales.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.service.bill.BillService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bill")
@Api(tags = "Bill Management", description = "Endpoints for managing bills")
public class BillController {
    
    @Autowired
    private BillService billService;

    @GetMapping("/all")
    @Operation(summary = "Get all bills", description = "Retrieves a list of all bills")
    @ApiResponse(responseCode = "200", description = "Bills retrieved successfully")
    public ResponseEntity<List<BillDTO>> getAllBills() {
        List<BillDTO> bills = billService.getAll();
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @GetMapping("/allByBranch/{branchId}")
    @Operation(summary = "Get all bills by branch", description = "Retrieves a list of all bills by branch")
    @ApiResponse(responseCode = "200", description = "Bills retrieved successfully")
    public ResponseEntity<List<BillDTO>> getAllBillsByBranch(@PathVariable Long branchId) {
        List<BillDTO> bills = billService.findByBranchId(branchId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }
    
    @PostMapping("/add")
    @Operation(summary = "Add a new bill", description = "Adds a new bill")
    @ApiResponse(responseCode = "201", description = "Bill added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<?> addBill(@RequestBody BillDTO billDTO) {
        BillDTO newBill = billService.save(billDTO);
        if (newBill == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newBill, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get a bill by ID", description = "Retrieves a bill by its ID")
    @ApiResponse(responseCode = "200", description = "Bill retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Bill not found")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<BillDTO> bill = billService.getById(id);
        return bill.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Update a bill", description = "Updates an existing bill")
    @ApiResponse(responseCode = "200", description = "Bill updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Bill not found")
    public ResponseEntity<?> updateBill(@PathVariable Long id, @RequestBody BillDTO billDTO) {
        if (id == null || billDTO == null) {
            System.out.println("ID or billDTO is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Bill updatedBill = billService.update(id, billDTO);
        if(updatedBill != null){
            return new ResponseEntity<>(updatedBill, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.emptyList(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a bill", description = "Deletes a bill")
    @ApiResponse(responseCode = "200", description = "Bill deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Bill not found")
    public ResponseEntity<?> deleteBill(@PathVariable Long id) {
        if (billService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
    }

}
