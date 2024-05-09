package com.stockwage.commercial.sales.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ProductUpdateListener {

    @RabbitListener(queues = "update-product-queue")
    public void processProductUpdate(@Payload String message) {
        // Divide el mensaje para obtener el ID y la cantidad
        String[] parts = message.split(",");
        Long productId = Long.parseLong(parts[0]);
        Integer quantity = Integer.parseInt(parts[1]);
        System.out.println("Product quantity updated: ID=" + productId + ", Quantity=" + quantity);
    }
}
