package com.stockwage.commercial.sales.rabbitmq.listener;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockwage.commercial.sales.dto.ProductDTO;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.service.product.ProductService;

@Component
public class ProductCreateUpdateListener {

    @Autowired
    ProductService productService;

    @RabbitListener(queues = "create-update-product-queue")
    public void processProductUpdate(@Payload String message) {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = null;
        try {
            // Convert the message to ProductDTO
            productDTO = objectMapper.readValue(message, ProductDTO.class);
            
            // Check if the product exists
            Optional<Product> existingProduct = productService.getById(productDTO.getId());
            if (existingProduct.isPresent()) {
                // If the product exists, update it
                Product existing = existingProduct.get();
                existing.setName(productDTO.getName());
                existing.setDescription(productDTO.getDescription());
                existing.setSalePrice(productDTO.getSalePrice());
                existing.setCategoryId(productDTO.getCategoryId());
                productService.update(existing);
                System.out.println("Product with ID " + productDTO.getId() + " updated.");
            } else {
                // If the product does not exist, create it
                Product newProduct = productService.DtoToEntity(productDTO);
                productService.save(newProduct);
                System.out.println("Product with ID " + productDTO.getId() + " created.");
            }
        } catch (Exception e) {
            // Print an error message if an exception occurs during message processing
            e.printStackTrace();
            System.err.println("Error processing message: " + message);
        }
    }
}
