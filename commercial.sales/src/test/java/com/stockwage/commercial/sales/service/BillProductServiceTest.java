package com.stockwage.commercial.sales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stockwage.commercial.sales.dto.BillProductDTO;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.BillProduct;
import com.stockwage.commercial.sales.entity.BranchProduct;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.repository.BillProductRepository;
import com.stockwage.commercial.sales.repository.BillRepository;
import com.stockwage.commercial.sales.repository.BranchProductRepository;
import com.stockwage.commercial.sales.repository.ProductRepository;
import com.stockwage.commercial.sales.service.billproduct.BillProductServiceImpl;
import com.stockwage.commercial.sales.service.branchproduct.BranchProductService;
import com.stockwage.commercial.sales.rabbitmq.producer.branchproduct.BranchProductQuantityUpdateProducer;

@ExtendWith(MockitoExtension.class)
public class BillProductServiceTest {

    @InjectMocks
    private BillProductServiceImpl billProductService;

    @Mock
    private BillProductRepository billProductRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchProductRepository branchProductRepository;

    @Mock
    private BranchProductService branchProductService;

    @Mock
    private BranchProductQuantityUpdateProducer branchProductQuantityUpdateProducer;

    @Test
    public void testSave_ValidBillProductDTOAndExistingBillAndProduct_ReturnsSavedBillProduct() {
        // Given
        Long billId = 1L;
        Long productId = 1L;
        BillProductDTO billProductDTO = new BillProductDTO();
        billProductDTO.setProduct_id(productId);
        billProductDTO.setQuantity(5);

        Bill bill = new Bill();
        bill.setId(billId);
        bill.setBranchId(1L);
        Product product = new Product();
        product.setId(productId);
        product.setSalePrice(10.0);
        BranchProduct branchProduct = new BranchProduct();
        branchProduct.setBranchId(1L);
        branchProduct.setQuantity(10);
        branchProduct.setDiscount(0);

        when(billRepository.findById(billId)).thenReturn(Optional.of(bill));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(branchProductRepository.findByProductIdAndBranchId(productId, bill.getBranchId())).thenReturn(branchProduct);
        when(billProductRepository.save(any(BillProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(branchProductService.updateQuantity(productId, bill.getBranchId(), 5)).thenReturn(true);

        // When
        BillProduct result = billProductService.save(billProductDTO, billId);

        // Then
        assertNotNull(result);
        assertEquals(bill, result.getBill());
        assertEquals(product, result.getProduct());
        assertEquals(billProductDTO.getQuantity(), result.getQuantity());
        assertEquals(product.getSalePrice(), result.getUnitPrice());
        assertEquals(branchProduct.getDiscount(), result.getDiscountPercentage());
    }

    @Test
    public void testSave_InvalidBillProductDTO_ReturnsNull() {
        // Given
        Long billId = 1L;
        BillProductDTO billProductDTO = new BillProductDTO();
        billProductDTO.setProduct_id(null); // Invalid DTO

        // When
        BillProduct result = billProductService.save(billProductDTO, billId);

        // Then
        assertNull(result);
    }

    @Test
    public void testSave_NonExistingBill_ReturnsNull() {
        // Given
        Long billId = 1L;
        Long productId = 1L;
        BillProductDTO billProductDTO = new BillProductDTO();
        billProductDTO.setProduct_id(productId);

        when(billRepository.findById(billId)).thenReturn(Optional.empty());

        // When
        BillProduct result = billProductService.save(billProductDTO, billId);

        // Then
        assertNull(result);
    }

    @Test
    public void testSave_NonExistingProduct_ReturnsNull() {
        // Given
        Long billId = 1L;
        Long productId = 1L;
        BillProductDTO billProductDTO = new BillProductDTO();
        billProductDTO.setProduct_id(productId);

        Bill bill = new Bill();
        bill.setId(billId);
        bill.setBranchId(1L);

        when(billRepository.findById(billId)).thenReturn(Optional.of(bill));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        BillProduct result = billProductService.save(billProductDTO, billId);

        // Then
        assertNull(result);
    }

    @Test
    public void testSave_BranchProductNotFound_ReturnsNull() {
        // Given
        Long billId = 1L;
        Long productId = 1L;
        BillProductDTO billProductDTO = new BillProductDTO();
        billProductDTO.setProduct_id(productId);

        Bill bill = new Bill();
        bill.setId(billId);
        bill.setBranchId(1L);
        Product product = new Product();
        product.setId(productId);

        when(billRepository.findById(billId)).thenReturn(Optional.of(bill));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(branchProductRepository.findByProductIdAndBranchId(productId, bill.getBranchId())).thenReturn(null);

        // When
        BillProduct result = billProductService.save(billProductDTO, billId);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetAll_ReturnsListOfBillProductDTO() {
        // Given
        List<BillProduct> billProducts = new ArrayList<>();
        BillProduct billProduct1 = new BillProduct();
        billProduct1.setBill(new Bill()); 
        billProduct1.setProduct(new Product());
        billProduct1.setQuantity(5);
        billProduct1.setDiscountPercentage(0);
        billProducts.add(billProduct1);
    
        when(billProductRepository.findAll()).thenReturn(billProducts);
    
        // When
        List<BillProductDTO> result = billProductService.getAll();
    
        // Then
        assertNotNull(result);
        assertEquals(billProducts.size(), result.size());
    }
    
    @Test
    public void testGetAllByBill_ReturnsListOfBillProductDTO() {
        // Given
        Long billId = 1L;
        List<BillProduct> billProducts = new ArrayList<>();
        BillProduct billProduct1 = new BillProduct();
        billProduct1.setBill(new Bill()); 
        billProduct1.setProduct(new Product());
        billProduct1.setQuantity(5);
        billProduct1.setDiscountPercentage(0);
        billProducts.add(billProduct1);
    
        when(billProductRepository.findByBillId(billId)).thenReturn(billProducts);
    
        // When
        List<BillProductDTO> result = billProductService.getAllByBill(billId);
    
        // Then
        assertNotNull(result);
        assertEquals(billProducts.size(), result.size());
    }
    
}
