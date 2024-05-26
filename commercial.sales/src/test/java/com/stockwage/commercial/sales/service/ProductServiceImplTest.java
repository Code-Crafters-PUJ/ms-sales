package com.stockwage.commercial.sales.service;

import com.stockwage.commercial.sales.dto.ProductDTO;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.repository.ProductRepository;
import com.stockwage.commercial.sales.service.product.ProductServiceImpl;

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
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testDtoToEntity() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setSalePrice(10.0);
        productDTO.setCategoryId(1);

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setSalePrice(10.0);
        product.setCategoryId(1);

        when(modelMapper.map(productDTO, Product.class)).thenReturn(product);

        Product result = productService.DtoToEntity(productDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(10.0, result.getSalePrice());
        assertEquals(1, result.getCategoryId());

        verify(modelMapper, times(1)).map(productDTO, Product.class);
    }

    @Test
    public void testGetById() {
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setSalePrice(10.0);
        product.setCategoryId(1);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("Test Product", result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals(10.0, result.get().getSalePrice());
        assertEquals(1, result.get().getCategoryId());

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    public void testSave() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setSalePrice(10.0);
        product.setCategoryId(1);

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.save(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(10.0, result.getSalePrice());
        assertEquals(1, result.getCategoryId());

        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        Product product = new Product();
        product.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        assertTrue(productService.delete(id));

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdate() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setSalePrice(10.0);
        product.setCategoryId(1);

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.update(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(10.0, result.getSalePrice());
        assertEquals(1, result.getCategoryId());

        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetAll() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(productRepository, times(1)).findAll();
    }
}
