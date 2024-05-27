package com.stockwage.commercial.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockwage.commercial.sales.dto.PaymentMethodDTO;
import com.stockwage.commercial.sales.entity.PaymentMethod;
import com.stockwage.commercial.sales.service.paymentmethod.PaymentMethodService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PaymentMethodControllerUnitTest {

    @Mock
    private PaymentMethodService paymentMethodService;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentMethodController).build();
    }

    @Test
    public void testGetAllPaymentMethods() throws Exception {
        PaymentMethod paymentMethod1 = new PaymentMethod();
        paymentMethod1.setId(1L);
        paymentMethod1.setMethod("Credit Card");

        PaymentMethod paymentMethod2 = new PaymentMethod();
        paymentMethod2.setId(2L);
        paymentMethod2.setMethod("Debit Card");

        when(paymentMethodService.getAll()).thenReturn(Arrays.asList(paymentMethod1, paymentMethod2));

        mockMvc.perform(get("/paymentMethod/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].method").value("Credit Card"))
                .andExpect(jsonPath("$[1].method").value("Debit Card"));
    }

    @Test
    public void testAddPaymentMethod() throws Exception {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setMethod("Credit Card");

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setMethod("Credit Card");

        when(paymentMethodService.DtoToEntity(any(PaymentMethodDTO.class))).thenReturn(paymentMethod);
        when(paymentMethodService.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        mockMvc.perform(post("/paymentMethod/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentMethodDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.method").value("Credit Card"));
    }

    @Test
    public void testAddPaymentMethodDuplicateKeyException() throws Exception {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setMethod("Credit Card");

        when(paymentMethodService.DtoToEntity(any(PaymentMethodDTO.class))).thenReturn(new PaymentMethod());
        when(paymentMethodService.save(any(PaymentMethod.class))).thenThrow(DuplicateKeyException.class);

        mockMvc.perform(post("/paymentMethod/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentMethodDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetPaymentMethodById() throws Exception {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setMethod("Credit Card");

        when(paymentMethodService.getById(1L)).thenReturn(Optional.of(paymentMethod));

        mockMvc.perform(get("/paymentMethod/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.method").value("Credit Card"));
    }

    @Test
    public void testGetPaymentMethodByIdNotFound() throws Exception {
        when(paymentMethodService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/paymentMethod/get/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePaymentMethod() throws Exception {
        PaymentMethodDTO updatedPaymentMethodDTO = new PaymentMethodDTO();
        updatedPaymentMethodDTO.setMethod("Updated Method");

        PaymentMethod updatedPaymentMethod = new PaymentMethod();
        updatedPaymentMethod.setId(1L);
        updatedPaymentMethod.setMethod("Updated Method");

        when(paymentMethodService.getById(1L)).thenReturn(Optional.of(new PaymentMethod()));
        when(paymentMethodService.DtoToEntity(any(PaymentMethodDTO.class))).thenReturn(updatedPaymentMethod);
        when(paymentMethodService.update(any(PaymentMethod.class))).thenReturn(updatedPaymentMethod);

        mockMvc.perform(put("/paymentMethod/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPaymentMethodDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.method").value("Updated Method"));
    }

    @Test
    public void testDeletePaymentMethod() throws Exception {
        when(paymentMethodService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/paymentMethod/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletePaymentMethodNotFound() throws Exception {
        when(paymentMethodService.delete(1L)).thenReturn(false);

        mockMvc.perform(delete("/paymentMethod/delete/1"))
                .andExpect(status().isNotFound());
    }
}
