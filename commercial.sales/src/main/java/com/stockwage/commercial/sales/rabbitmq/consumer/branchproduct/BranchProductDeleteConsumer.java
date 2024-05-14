package com.stockwage.commercial.sales.rabbitmq.consumer.branchproduct;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.stockwage.commercial.sales.entity.BranchProduct;
import com.stockwage.commercial.sales.service.branchproduct.BranchProductService;

@Component
public class BranchProductDeleteConsumer {
    @Autowired
    BranchProductService branchProductService;

    @RabbitListener(queues = "delete-branch-product-queue")
    public void processBranchProductDelete(@Payload String message) {
        String[] parts = message.split(",");
        if (parts.length != 2) {
            System.err.println("Invalid message format: " + message);
            return;
        }

        try {
            Long branchId = Long.valueOf(parts[0].trim());
            Long productId = Long.valueOf(parts[1].trim());
            System.out.println(message);
            Optional<BranchProduct> optionalBranchProduct = branchProductService.findByProductIdAndBranchId(productId, branchId);
            if (!optionalBranchProduct.isPresent()) {
                System.err.println("Branch product with Branch ID " + branchId + " and Product ID " + productId + " not found.");
                return;
            }
            BranchProduct branchProduct = optionalBranchProduct.get();
            // Delete the branch product with the provided IDs
            branchProductService.delete(branchProduct.getId());
            System.out.println("Branch product with Branch ID " + branchId + " and Product ID " + productId + " deleted.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid branchId or productId format in message: " + message);
        } catch (Exception e) {
            // Handle other exceptions here
            e.printStackTrace();
            System.err.println("Error deleting branch product with message: " + message);
        }
    }
}



//