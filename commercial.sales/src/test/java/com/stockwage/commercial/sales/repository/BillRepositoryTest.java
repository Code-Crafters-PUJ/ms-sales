package com.stockwage.commercial.sales.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.stockwage.commercial.sales.entity.Bill;

@DataJpaTest
public class BillRepositoryTest {

    private BillRepository billRepository;

    @BeforeEach
    public void setUp() {
        // Create a mock of the repository
        billRepository = mock(BillRepository.class);
    }

    @Test
    public void testFindByBranchId() {
        // Create test data
        Long branchId = 1L;
        Bill bill1 = new Bill();
        bill1.setId(1L);
        bill1.setBranchId(branchId);
        Bill bill2 = new Bill();
        bill2.setId(2L);
        bill2.setBranchId(branchId);
        List<Bill> bills = new ArrayList<>();
        bills.add(bill1);
        bills.add(bill2);

        // Configure the mock behavior
        when(billRepository.findByBranchId(branchId)).thenReturn(bills);

        // Call the method under test
        List<Bill> result = billRepository.findByBranchId(branchId);

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }


    @Test
    public void testSave() {
        // Create test data
        Bill bill = new Bill();
        bill.setId(1L);
        bill.setBranchId(1L);

        // Configure the mock behavior
        when(billRepository.save(bill)).thenReturn(bill);

        // Call the method under test
        Bill result = billRepository.save(bill);

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void testFindById() {
        // Create test data
        Long billId = 1L;
        Bill bill = new Bill();
        bill.setId(billId);
        bill.setBranchId(1L);

        // Configure the mock behavior
        when(billRepository.findById(billId)).thenReturn(java.util.Optional.of(bill));

        // Call the method under test
        java.util.Optional<Bill> result = billRepository.findById(billId);

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    public void testUpdate() {
        // Create test data
        Long billId = 1L;
        Bill bill = new Bill();
        bill.setId(billId);
        bill.setBranchId(1L);
        
        // Configure the mock behavior
        when(billRepository.existsById(billId)).thenReturn(true);
        when(billRepository.save(bill)).thenReturn(bill);

        // Call the method under test
        Bill result = billRepository.save(bill);

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void testDelete() {
        // Create test data
        Long billId = 1L;
        Bill bill = new Bill();
        bill.setId(billId);
        bill.setBranchId(1L);

        // Call the method under test
        billRepository.delete(bill);

        // Verify the deletion by checking if findById returns null
        assertThat(billRepository.findById(billId)).isNotPresent();
    }

    @Test
    public void testFindAll() {
        // Create test data
        List<Bill> bills = new ArrayList<>();
        Bill bill1 = new Bill();
        bill1.setId(1L);
        bill1.setBranchId(1L);
        Bill bill2 = new Bill();
        bill2.setId(2L);
        bill2.setBranchId(2L);
        bills.add(bill1);
        bills.add(bill2);

        // Configure the mock behavior
        when(billRepository.findAll()).thenReturn(bills);

        // Call the method under test
        List<Bill> result = billRepository.findAll();

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }
}
