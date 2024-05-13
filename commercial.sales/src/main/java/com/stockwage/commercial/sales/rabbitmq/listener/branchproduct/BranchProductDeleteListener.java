package com.stockwage.commercial.sales.rabbitmq.listener.branchproduct;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.stockwage.commercial.sales.service.branchproduct.BranchProductService;

@Component
public class BranchProductDeleteListener {
    @Autowired
    BranchProductService branchProductService;

    @RabbitListener(queues = "delete-branch-product-queue")
    public void processBranchProductDelete(@Payload String stringBranchProductId) {
        Long branchProductId = Long.valueOf(stringBranchProductId);
        System.out.println("Receive delete message: " + branchProductId);
        try {
            // Delete the branch product with the provided ID
            branchProductService.delete(branchProductId);
            System.out.println("Branch product with ID " + branchProductId + " deleted.");
        } catch (Exception e) {
            // Handle errors when deleting branch products
            e.printStackTrace();
            System.err.println("Error deleting branch product with ID " + branchProductId);
        }
    }
}
