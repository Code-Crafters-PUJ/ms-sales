package com.stockwage.commercial.sales.rabbitmq;

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
public class ProductUpdateListener {

    @Autowired
    ProductService productService;

    @RabbitListener(queues = "update-product-queue")
    public void processProductUpdate(@Payload String message) {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = null;
        try {
            productDTO = objectMapper.readValue(message, ProductDTO.class);
            Optional<Product> existingProduct = productService.getById(productDTO.getId());
            if (existingProduct.isPresent()) {
                Product product = productService.DtoToEntity(productDTO);
                productService.update(product);
            } else {
                System.err.println("The product with ID " + productDTO.getId() + " does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error processing message: " + message);
        }
    }
}
