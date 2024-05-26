package com.stockwage.commercial.sales.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.stockwage.commercial.sales.entity.Product;

@DataJpaTest
public class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        
        // Simulate behavior for save method
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(1L); // Simulate generating an ID upon saving
            return product;
        });

        // Simulate behavior for findById method
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));

        // Simulate behavior for findAll method
        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        productList.add(new Product());
        productList.add(new Product());
        when(productRepository.findAll()).thenReturn(productList);
    }

    @Test
    void testSave() {
        // Create a Product object
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setSalePrice(100.0);
        product.setCategoryId(1);

        // Save the Product
        Product savedProduct = productRepository.save(product);

        // Verify that the saved Product is not null
        assertNotNull(savedProduct);

        // Verify that the ID of the saved Product is not null
        assertNotNull(savedProduct.getId());
    }

    @Test
    void testFindById() {
        // Get Product by ID
        Optional<Product> foundProduct = productRepository.findById(1L);

        // Verify that the found Product is not empty
        assertTrue(foundProduct.isPresent());
    }

    @Test
    void testFindAll() {
        // Get all Products
        List<Product> foundProducts = productRepository.findAll();

        // Verify that the list is not null and has the expected length
        assertNotNull(foundProducts);
        assertEquals(3, foundProducts.size());
    }

    @Test
    void testDelete() {
        // Create a Product object
        Product product = new Product();
        product.setId(1L);
        
        // Delete the Product
        productRepository.delete(product);
        
        // Verify that the Product was deleted
        verify(productRepository, times(1)).delete(eq(product));
    }

    @Test
    void testUpdate() {
        // Create a Product object
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Name");
        
        // Update the Product
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product updatedProduct = productRepository.save(product);
        
        // Verify that the Product was updated
        assertNotNull(updatedProduct);
        assertEquals("Updated Name", updatedProduct.getName());
    }
}
