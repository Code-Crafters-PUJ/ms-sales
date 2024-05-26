package com.stockwage.commercial.sales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.stockwage.commercial.sales.dto.BranchProductDTO;
import com.stockwage.commercial.sales.entity.BranchProduct;
import com.stockwage.commercial.sales.repository.BranchProductRepository;
import com.stockwage.commercial.sales.service.branchproduct.BranchProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BranchProductServiceTest {

    @InjectMocks
    private BranchProductServiceImpl branchProductService;

    @Mock
    private BranchProductRepository branchProductRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testGetById_ReturnsBranchProduct_WhenFound() {
        // Given
        Long id = 1L;
        BranchProduct branchProduct = new BranchProduct();
        branchProduct.setId(id);

        when(branchProductRepository.findById(id)).thenReturn(Optional.of(branchProduct));

        // When
        Optional<BranchProduct> result = branchProductService.getById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(branchProduct, result.get());
    }

    @Test
    public void testGetById_ReturnsEmptyOptional_WhenNotFound() {
        // Given
        Long id = 1L;

        when(branchProductRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<BranchProduct> result = branchProductService.getById(id);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSave_ReturnsSavedBranchProduct() {
        // Given
        BranchProduct branchProduct = new BranchProduct();
        branchProduct.setBranchId(1L);
        branchProduct.setQuantity(10);
        branchProduct.setDiscount(0);

        when(branchProductRepository.save(any(BranchProduct.class))).thenReturn(branchProduct);

        // When
        BranchProduct result = branchProductService.save(branchProduct);

        // Then
        assertNotNull(result);
        assertEquals(branchProduct, result);
    }

    @Test
    public void testDelete_ReturnsTrue_WhenBranchProductFound() {
        // Given
        Long id = 1L;
        BranchProduct branchProduct = new BranchProduct();

        when(branchProductRepository.findById(id)).thenReturn(Optional.of(branchProduct));

        // When
        boolean result = branchProductService.delete(id);

        // Then
        assertTrue(result);
    }

    @Test
    public void testDelete_ReturnsFalse_WhenBranchProductNotFound() {
        // Given
        Long id = 1L;

        when(branchProductRepository.findById(id)).thenReturn(Optional.empty());

        // When
        boolean result = branchProductService.delete(id);

        // Then
        assertFalse(result);
    }

    @Test
    public void testUpdate_ReturnsUpdatedBranchProduct() {
        // Given
        BranchProduct branchProduct = new BranchProduct();
        branchProduct.setId(1L);
        branchProduct.setBranchId(1L);
        branchProduct.setQuantity(10);
        branchProduct.setDiscount(0);

        when(branchProductRepository.save(any(BranchProduct.class))).thenReturn(branchProduct);

        // When
        BranchProduct result = branchProductService.update(branchProduct);

        // Then
        assertNotNull(result);
        assertEquals(branchProduct, result);
    }

    @Test
    public void testGetAll_ReturnsListOfBranchProducts() {
        // Given
        List<BranchProduct> expectedBranchProducts = new ArrayList<>();
        expectedBranchProducts.add(new BranchProduct());
        expectedBranchProducts.add(new BranchProduct());
        when(branchProductRepository.findAll()).thenReturn(expectedBranchProducts);

        // When
        List<BranchProduct> result = branchProductService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(expectedBranchProducts.size(), result.size());
        assertEquals(expectedBranchProducts, result);
    }

    @Test
    public void testDtoToEntity_ConvertsDTOToEntity() {
        // Given
        BranchProductDTO branchProductDTO = new BranchProductDTO();
        branchProductDTO.setBranchId(1L);
        branchProductDTO.setProductId(2L);
        branchProductDTO.setQuantity(10);
        branchProductDTO.setDiscount(5);
    
        when(modelMapper.map(any(), eq(BranchProduct.class))).thenAnswer(invocation -> {
            BranchProductDTO dto = invocation.getArgument(0);
            BranchProduct branchProduct = new BranchProduct();
            branchProduct.setBranchId(dto.getBranchId());
            branchProduct.setQuantity(dto.getQuantity());
            branchProduct.setDiscount(dto.getDiscount());
            return branchProduct;
        });
    
        // When
        BranchProduct result = branchProductService.DtoToEntity(branchProductDTO);
    
        // Then
        assertNotNull(result);
        assertEquals(branchProductDTO.getBranchId(), result.getBranchId());
        assertEquals(branchProductDTO.getQuantity(), result.getQuantity());
        assertEquals(branchProductDTO.getDiscount(), result.getDiscount());
    }
    


    @Test
    public void testUpdateQuantity_ReturnsFalse_WhenBranchProductNotFound() {
        // Given
        Long productId = 1L;
        Long branchId = 1L;
        Integer newQuantity = 20;

        when(branchProductRepository.findByProductIdAndBranchId(productId, branchId)).thenReturn(null);

        // When
        boolean result = branchProductService.updateQuantity(productId, branchId, newQuantity);

        // Then
        assertFalse(result);
        verify(branchProductRepository, never()).save(any());
    }

    @Test
    public void testFindByProductIdAndBranchId_ReturnsBranchProduct_WhenFound() {
        // Given
        Long productId = 1L;
        Long branchId = 1L;
        BranchProduct branchProduct = new BranchProduct();
        branchProduct.setBranchId(branchId);

        when(branchProductRepository.findByProductIdAndBranchId(productId, branchId)).thenReturn(branchProduct);

        // When
        Optional<BranchProduct> result = branchProductService.findByProductIdAndBranchId(productId, branchId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(branchProduct, result.get());
    }

    @Test
    public void testFindByProductIdAndBranchId_ReturnsEmptyOptional_WhenNotFound() {
        // Given
        Long productId = 1L;
        Long branchId = 1L;

        when(branchProductRepository.findByProductIdAndBranchId(productId, branchId)).thenReturn(null);

        // When
        Optional<BranchProduct> result = branchProductService.findByProductIdAndBranchId(productId, branchId);

        // Then
        assertTrue(result.isEmpty());
    }
}
