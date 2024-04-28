package com.stockwage.commercial.sales.service.email;

import org.springframework.http.ResponseEntity;

public interface EmailService {
    public ResponseEntity<String> sendEmail(Long id);
}
