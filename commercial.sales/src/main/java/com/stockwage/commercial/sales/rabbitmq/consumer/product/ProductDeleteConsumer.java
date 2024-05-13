package com.stockwage.commercial.sales.rabbitmq.consumer.product;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.stockwage.commercial.sales.service.product.ProductService;

@Component
public class ProductDeleteConsumer {

    @Autowired
    ProductService productService;

    @RabbitListener(queues = "delete-product-queue")
    public void processProductDelete(@Payload String stringProductId) {
        Long productId = Long.valueOf(stringProductId);
        System.out.println("Receive delete message: " + productId);
        try {
            // Delete the product with the provided ID
            productService.delete(productId);
            System.out.println("Product with ID " + productId + " deleted.");
        } catch (Exception e) {
            // Handle errors when deleting products
            e.printStackTrace();
            System.err.println("Error deleting product with ID " + productId);
        }
    }
}
