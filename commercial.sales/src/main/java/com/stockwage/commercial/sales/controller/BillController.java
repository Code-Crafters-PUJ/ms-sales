package com.stockwage.commercial.sales.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.service.bill.BillService;
import com.stockwage.commercial.sales.service.client.ClientService;

import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bill")
@Api(tags = "Bill Management", description = "Endpoints for managing bills")
@PreAuthorize("hasRole('Raiz') or hasRole('Ventas')")
public class BillController {
    
    @Autowired
    private BillService billService;

    @Autowired
    private ClientService clientService;


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

    @GetMapping("/getPDF/{billId}")
    @Operation(summary = "Get a PDF of a bill", description = "Get a PDF file for the bill")
    @ApiResponse(responseCode = "200", description = "PDF bill retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Bill not found")
    public ResponseEntity<byte[]> getPDF(@PathVariable Long billId) throws IOException {
        // Validate the bill ID
        if (billId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Retrieve the bill from the service
        Optional<BillDTO> optionalBill = billService.getById(billId);
        if (!optionalBill.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        BillDTO billDTO = optionalBill.get();

        // Retrieve the client information associated with the bill
        Optional<Client> optionalClient = clientService.getById(billDTO.getClientId());
        if (!optionalClient.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Client client = optionalClient.get();

        // File path of the PDF
        String pdfFilePath = "./bills/Bill-" + client.getCard_id() + "-" + billId + ".pdf";

        // Read the content of the PDF file into a byte array
        Path path = Paths.get(pdfFilePath);
        byte[] pdfContent = Files.readAllBytes(path);

        // Build the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "Bill No." + billId + ".pdf");

        // Return a ResponseEntity with the PDF content and configured headers
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfContent.length)
                .body(pdfContent);
    }

}