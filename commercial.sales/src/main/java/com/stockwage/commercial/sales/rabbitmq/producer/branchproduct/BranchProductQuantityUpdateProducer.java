package com.stockwage.commercial.sales.rabbitmq.producer.branchproduct;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BranchProductQuantityUpdateProducer {
    
    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendBranchProductQuantityUpdateMessage(Long productId, Long branchId, Integer newQuantity) {
        String queueName = "branch-product-quantity-update-queue";

        String message = productId + "," + branchId + "," + newQuantity;
        amqpTemplate.convertAndSend(queueName, message);
        System.out.println("Mensaje de actualización de cantidad de producto de sucursal enviado: " + message);
    }
}
