package com.stockwage.commercial.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockwage.commercial.sales.service.email.EmailService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/email")
@Api(tags = "Email Management", description = "Endpoint for sending emails")
public class EmailController {
    
    @Autowired
    private EmailService emailService;

    @PostMapping("/send{id}")
    public ResponseEntity<String> sendEmail(@PathVariable Long id) {
        ResponseEntity<String> response = emailService.sendEmail(id);
        return response;
    }
}
