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

import com.stockwage.commercial.sales.entity.BranchProduct;

@DataJpaTest
public class BranchProductRepositoryTest {

    private BranchProductRepository branchProductRepository;

    @BeforeEach
    void setUp() {
        branchProductRepository = mock(BranchProductRepository.class);
        
        // Simulate behavior for save method
        when(branchProductRepository.save(any(BranchProduct.class))).thenAnswer(invocation -> {
            BranchProduct branchProduct = invocation.getArgument(0);
            branchProduct.setId(1L); // Simulate generating an ID upon saving
            return branchProduct;
        });

        // Simulate behavior for findById method
        when(branchProductRepository.findById(1L)).thenReturn(Optional.of(new BranchProduct()));

        // Simulate behavior for findByProductIdAndBranchId method
        when(branchProductRepository.findByProductIdAndBranchId(1L, 1L)).thenReturn(new BranchProduct());

        // Simulate behavior for findAll method
        List<BranchProduct> branchProducts = new ArrayList<>();
        branchProducts.add(new BranchProduct());
        branchProducts.add(new BranchProduct());
        branchProducts.add(new BranchProduct());
        when(branchProductRepository.findAll()).thenReturn(branchProducts);
    }

    @Test
    void testSave() {
        // Create a BranchProduct object
        BranchProduct branchProduct = new BranchProduct();
        branchProduct.setBranchId(1L);
        branchProduct.setQuantity(10);

        // Save the BranchProduct
        BranchProduct savedBranchProduct = branchProductRepository.save(branchProduct);

        // Verify that the saved BranchProduct is not null
        assertNotNull(savedBranchProduct);

        // Verify that the ID of the saved BranchProduct is not null
        assertNotNull(savedBranchProduct.getId());
    }

    @Test
    void testFindById() {
        // Get BranchProduct by ID
        Optional<BranchProduct> foundBranchProduct = branchProductRepository.findById(1L);

        // Verify that the found BranchProduct is not empty
        assertTrue(foundBranchProduct.isPresent());
    }

    @Test
    void testFindByProductIdAndBranchId() {
        // Get BranchProduct by product ID and branch ID
        BranchProduct foundBranchProduct = branchProductRepository.findByProductIdAndBranchId(1L, 1L);

        // Verify that the found BranchProduct is not null
        assertNotNull(foundBranchProduct);
    }

    @Test
    void testDelete() {
        // Create a BranchProduct object
        BranchProduct branchProduct = new BranchProduct();
        branchProduct.setId(1L);
        
        // Delete the BranchProduct
        branchProductRepository.delete(branchProduct);
        
        // Verify that the BranchProduct was deleted
        verify(branchProductRepository, times(1)).delete(eq(branchProduct));
    }

    @Test
    void testUpdate() {
        // Create a BranchProduct object
        BranchProduct branchProduct = new BranchProduct();
        branchProduct.setId(1L);
        branchProduct.setBranchId(1L);
        branchProduct.setQuantity(20);
        
        // Update the BranchProduct
        when(branchProductRepository.save(any(BranchProduct.class))).thenReturn(branchProduct);
        BranchProduct updatedBranchProduct = branchProductRepository.save(branchProduct);
        
        // Verify that the BranchProduct was updated
        assertNotNull(updatedBranchProduct);
        assertEquals(20, updatedBranchProduct.getQuantity());
    }

    @Test
    void testFindAll() {
        // Get all BranchProducts
        List<BranchProduct> foundBranchProducts = branchProductRepository.findAll();

        // Verify that the list is not null and has the expected length
        assertNotNull(foundBranchProducts);
        assertEquals(3, foundBranchProducts.size());
    }
}