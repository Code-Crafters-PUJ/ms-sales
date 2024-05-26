package com.stockwage.commercial.sales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.BillTypeEnum;
import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.entity.PaymentMethod;
import com.stockwage.commercial.sales.repository.BillRepository;
import com.stockwage.commercial.sales.repository.ClientRepository;
import com.stockwage.commercial.sales.repository.PaymentMethodRepository;
import com.stockwage.commercial.sales.service.bill.BillServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {

    @InjectMocks
    private BillServiceImpl billService;

    @Mock
    private BillRepository billRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Test
    public void testGetById_ExistingId_ReturnsBillDTO() {
        // Given
        Long id = 1L;
        Bill bill = new Bill();
        bill.setId(id);
        bill.setType(BillTypeEnum.N);
        bill.setDate(LocalDate.now());
        bill.setSeller("John Doe");
        bill.setBranchId(1L);
        bill.setWithholdingTax(false);
        bill.setChargeTax(false);
        bill.setClient(new Client());
        bill.setPaymentMethod(new PaymentMethod());

        when(billRepository.findById(id)).thenReturn(Optional.of(bill));

        // When
        Optional<BillDTO> result = billService.getById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    public void testGetById_NonExistingId_ReturnsEmptyOptional() {
        // Given
        Long id = 1L;
        when(billRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<BillDTO> result = billService.getById(id);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSave_ValidBillDTO_ReturnsSavedBillDTO() {
        // Given
        BillDTO billDTO = new BillDTO();
        billDTO.setType("N");
        billDTO.setDate(LocalDate.now());
        billDTO.setSeller("John Doe");
        billDTO.setBranchId(1L);
        billDTO.setWithholdingTax(false);
        billDTO.setChargeTax(false);
        billDTO.setClientId(1L);
        billDTO.setPaymentMethodId(1L);

        Client client = new Client();
        client.setId(1L);
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);

        when(clientRepository.findById(billDTO.getClientId())).thenReturn(Optional.of(client));
        when(paymentMethodRepository.findById(billDTO.getPaymentMethodId())).thenReturn(Optional.of(paymentMethod));

        // When
        BillDTO savedBillDTO = billService.save(billDTO);

        // Then
        assertNotNull(savedBillDTO);
    }

    @Test
    public void testDelete_ExistingId_ReturnsTrue() {
        // Given
        Long id = 1L;
        when(billRepository.findById(id)).thenReturn(Optional.of(new Bill()));

        // When
        boolean result = billService.delete(id);

        // Then
        assertTrue(result);
        verify(billRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDelete_NonExistingId_ReturnsFalse() {
        // Given
        Long id = 1L;
        when(billRepository.findById(id)).thenReturn(Optional.empty());

        // When
        boolean result = billService.delete(id);

        // Then
        assertFalse(result);
        verify(billRepository, never()).deleteById(anyLong());
    }


    @Test
    public void testGetAll_ReturnsListOfBillDTO() {
        // Given
        List<Bill> bills = new ArrayList<>();
        Bill bill1 = new Bill();
        bill1.setId(1L);
        bill1.setType(BillTypeEnum.N);
        bill1.setDate(LocalDate.now());
        bill1.setSeller("John Doe");
        bill1.setBranchId(1L);
        bill1.setWithholdingTax(false);
        bill1.setChargeTax(false);
        bill1.setClient(new Client());
        bill1.setPaymentMethod(new PaymentMethod());
        bills.add(bill1);

        when(billRepository.findAll()).thenReturn(bills);

        // When
        List<BillDTO> result = billService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(bills.size(), result.size());
    }

    @Test
    public void testFindByBranchId_ReturnsListOfBillDTO() {
        // Given
        Long branchId = 1L;
        List<Bill> bills = new ArrayList<>();
        Bill bill1 = new Bill();
        bill1.setId(1L);
        bill1.setType(BillTypeEnum.N);
        bill1.setDate(LocalDate.now());
        bill1.setSeller("John Doe");
        bill1.setBranchId(branchId);
        bill1.setWithholdingTax(false);
        bill1.setChargeTax(false);
        bill1.setClient(new Client());
        bill1.setPaymentMethod(new PaymentMethod());
        bills.add(bill1);

        when(billRepository.findByBranchId(branchId)).thenReturn(bills);

        // When
        List<BillDTO> result = billService.findByBranchId(branchId);

        // Then
        assertNotNull(result);
        assertEquals(bills.size(), result.size());
    }

    @Test
    public void testUpdate_ExistingIdAndValidBillDTO_ReturnsUpdatedBill() {
        // Given
        Long id = 1L;
        BillDTO billDTO = new BillDTO();
        billDTO.setType("N");
        billDTO.setDate(LocalDate.now());
        billDTO.setSeller("John Doe");
        billDTO.setBranchId(1L);
        billDTO.setWithholdingTax(false);
        billDTO.setChargeTax(false);
        billDTO.setClientId(1L);
        billDTO.setPaymentMethodId(1L);

        Client client = new Client();
        client.setId(1L);
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        
        Bill existingBill = new Bill();
        existingBill.setId(id);

        when(clientRepository.findById(billDTO.getClientId())).thenReturn(Optional.of(client));
        when(paymentMethodRepository.findById(billDTO.getPaymentMethodId())).thenReturn(Optional.of(paymentMethod));
        when(billRepository.findById(id)).thenReturn(Optional.of(existingBill));
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Mocking save operation

        // When
        Bill result = billService.update(id, billDTO);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(billDTO.getType(), result.getType().name());
        assertEquals(billDTO.getDate(), result.getDate());
        assertEquals(billDTO.getSeller(), result.getSeller());
        assertEquals(billDTO.getBranchId(), result.getBranchId());
        assertEquals(billDTO.isWithholdingTax(), result.isWithholdingTax());
        assertEquals(billDTO.isChargeTax(), result.isChargeTax());
        assertEquals(client, result.getClient());
        assertEquals(paymentMethod, result.getPaymentMethod());
    }

}
