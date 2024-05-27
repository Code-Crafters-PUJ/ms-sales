package com.stockwage.commercial.sales.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.service.bill.BillService;
import com.stockwage.commercial.sales.service.email.EmailService;

import java.util.Optional;

public class EmailControllerUnitTest {

    @Mock
    private EmailService emailService;

    @Mock
    private BillService billService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmail_InvalidId() {
        // Act
        ResponseEntity<String> response = emailController.sendEmail(null);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testSendEmail_BillNotFound() {
        // Arrange
        Long billId = 1L;
        when(billService.getById(billId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = emailController.sendEmail(billId);

        // Assert
        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(emailService, never()).sendEmail(anyLong());
    }

    @Test
    public void testSendEmail_Success() {
        // Arrange
        Long billId = 1L;
        BillDTO billDTO = new BillDTO();
        when(billService.getById(billId)).thenReturn(Optional.of(billDTO));
        when(emailService.sendEmail(billId)).thenReturn(new ResponseEntity<>("Email sent successfully", OK));

        // Act
        ResponseEntity<String> response = emailController.sendEmail(billId);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals("Email sent successfully", response.getBody());
        verify(emailService, times(1)).sendEmail(billId);
    }
}
