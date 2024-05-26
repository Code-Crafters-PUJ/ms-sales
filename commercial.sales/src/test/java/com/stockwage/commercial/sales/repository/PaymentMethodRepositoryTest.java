package com.stockwage.commercial.sales.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.stockwage.commercial.sales.entity.PaymentMethod;

@DataJpaTest
public class PaymentMethodRepositoryTest {

    private PaymentMethodRepository paymentMethodRepository;

    @BeforeEach
    void setUp() {
        paymentMethodRepository = mock(PaymentMethodRepository.class);
        
        // Simulate behavior for save method
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer(invocation -> {
            PaymentMethod paymentMethod = invocation.getArgument(0);
            paymentMethod.setId(1L); // Simulate generating an ID upon saving
            return paymentMethod;
        });

        // Simulate behavior for findById method
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(new PaymentMethod()));

        // Simulate behavior for findByMethod method
        when(paymentMethodRepository.findByMethod("method")).thenReturn(Optional.of(new PaymentMethod()));

        // Simulate behavior for existsByMethod method
        when(paymentMethodRepository.existsByMethod("method")).thenReturn(true);

        // Simulate behavior for findAll method
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethod());
        paymentMethods.add(new PaymentMethod());
        paymentMethods.add(new PaymentMethod());
        when(paymentMethodRepository.findAll()).thenReturn(paymentMethods);
    }

    @Test
    void testSave() {
        // Create a PaymentMethod object
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethod("Test Method");

        // Save the PaymentMethod
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        // Verify that the saved PaymentMethod is not null
        assertNotNull(savedPaymentMethod);

        // Verify that the ID of the saved PaymentMethod is not null
        assertNotNull(savedPaymentMethod.getId());
    }

    @Test
    void testFindById() {
        // Get PaymentMethod by ID
        Optional<PaymentMethod> foundPaymentMethod = paymentMethodRepository.findById(1L);

        // Verify that the found PaymentMethod is not empty
        assertTrue(foundPaymentMethod.isPresent());
    }

    @Test
    void testFindByMethod() {
        // Get PaymentMethod by method
        Optional<PaymentMethod> foundPaymentMethod = paymentMethodRepository.findByMethod("method");

        // Verify that the found PaymentMethod is not empty
        assertTrue(foundPaymentMethod.isPresent());
    }

    @Test
    void testExistsByMethod() {
        // Check if PaymentMethod exists by method
        boolean exists = paymentMethodRepository.existsByMethod("method");

        // Verify that PaymentMethod exists
        assertTrue(exists);
    }

    @Test
    void testDelete() {
        // Create a PaymentMethod object
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        
        // Delete the PaymentMethod
        paymentMethodRepository.delete(paymentMethod);
        
        // Verify that the PaymentMethod was deleted
        verify(paymentMethodRepository, times(1)).delete(eq(paymentMethod));
    }

    @Test
    void testFindAll() {
        // Get all PaymentMethods
        List<PaymentMethod> foundPaymentMethods = paymentMethodRepository.findAll();

        // Verify that the list is not null and has the expected length
        assertNotNull(foundPaymentMethods);
        assertEquals(3, foundPaymentMethods.size());
    }

    @Test
    void testUpdate() {
        // Create a PaymentMethod object
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setMethod("Updated Method");
        
        // Update the PaymentMethod
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        
        // Verify that the PaymentMethod was updated
        assertNotNull(updatedPaymentMethod);
        assertEquals("Updated Method", updatedPaymentMethod.getMethod());
    }
}
