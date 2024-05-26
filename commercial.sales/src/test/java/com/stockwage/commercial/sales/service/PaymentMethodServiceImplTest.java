package com.stockwage.commercial.sales.service;

import com.stockwage.commercial.sales.dto.PaymentMethodDTO;
import com.stockwage.commercial.sales.entity.PaymentMethod;
import com.stockwage.commercial.sales.repository.PaymentMethodRepository;
import com.stockwage.commercial.sales.service.paymentmethod.PaymentMethodServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodServiceImplTest {

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testDtoToEntity() {
        // Tu implementación anterior
    }

    @Test
    public void testGetById() {
        Long id = 1L;
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(id);

        when(paymentMethodRepository.findById(id)).thenReturn(Optional.of(paymentMethod));

        Optional<PaymentMethod> result = paymentMethodService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(paymentMethod, result.get());

        verify(paymentMethodRepository, times(1)).findById(id);
    }

    @Test
    public void testGetByMethod() {
        String method = "Credit Card";
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethod(method);

        when(paymentMethodRepository.findByMethod(method)).thenReturn(Optional.of(paymentMethod));

        Optional<PaymentMethod> result = paymentMethodService.getByMethod(method);

        assertTrue(result.isPresent());
        assertEquals(paymentMethod, result.get());

        verify(paymentMethodRepository, times(1)).findByMethod(method);
    }

    @Test
    public void testSave() {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setMethod("Credit Card");
    
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethod("Credit Card");
    
        when(paymentMethodRepository.existsByMethod(paymentMethod.getMethod())).thenReturn(false);
        when(paymentMethodRepository.save(paymentMethod)).thenReturn(paymentMethod);
    
        PaymentMethod result = paymentMethodService.save(paymentMethod);
    
        assertNotNull(result);
        assertEquals("Credit Card", result.getMethod());
    
        verify(paymentMethodRepository, times(1)).save(paymentMethod);
    }
    

    @Test
    public void testDelete() {
        Long id = 1L;
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(id);

        when(paymentMethodRepository.findById(id)).thenReturn(Optional.of(paymentMethod));

        assertTrue(paymentMethodService.delete(id));

        verify(paymentMethodRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdate() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setMethod("Credit Card");

        when(paymentMethodRepository.save(paymentMethod)).thenReturn(paymentMethod);

        PaymentMethod result = paymentMethodService.update(paymentMethod);

        assertNotNull(result);
        assertEquals("Credit Card", result.getMethod());

        verify(paymentMethodRepository, times(1)).save(paymentMethod);
    }

    @Test
    public void testGetAll() {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethod());
        paymentMethods.add(new PaymentMethod());

        when(paymentMethodRepository.findAll()).thenReturn(paymentMethods);

        List<PaymentMethod> result = paymentMethodService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(paymentMethodRepository, times(1)).findAll();
    }
}