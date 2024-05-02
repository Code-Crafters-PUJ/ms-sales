package com.stockwage.commercial.sales.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.service.bill.BillService;
import com.stockwage.commercial.sales.service.email.EmailService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/email")
@Api(tags = "Email Management", description = "Endpoint for sending emails")
public class EmailController {
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private BillService billService;

    @PostMapping("/send/{id}")
    @Operation(summary = "Send an email to a client", description = "Sends an email to the client with the specified ID")
    @ApiResponse(responseCode = "200", description = "Email sent successfully")
    @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Bill not found")
    public ResponseEntity<String> sendEmail(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<BillDTO> existingBillOptional = billService.getById(id);
        if (existingBillOptional.isPresent()) {
            ResponseEntity<String> response = emailService.sendEmail(id);
            return response;
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
