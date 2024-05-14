package com.stockwage.commercial.sales.rabbitmq.consumer.branchproduct;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockwage.commercial.sales.dto.BranchProductDTO;
import com.stockwage.commercial.sales.entity.BranchProduct;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.service.branchproduct.BranchProductService;
import com.stockwage.commercial.sales.service.product.ProductService;

@Component
public class BranchProductCreateUpdateConsumer {

    @Autowired
    BranchProductService branchProductService;

    @Autowired
    ProductService productService;

    @RabbitListener(queues = "create-update-branch-product-queue")
    public void processBranchProductUpdate(@Payload String message) {

        //message a Json, y JSON a BranchProductDTO
        ObjectMapper objectMapper = new ObjectMapper();
        BranchProductDTO branchProductDTO = null;
        try {
            // Convert the message to BranchProductDTO
            branchProductDTO = objectMapper.readValue(message, BranchProductDTO.class);

            Optional<BranchProduct> optionalBranchProduct = branchProductService.findByProductIdAndBranchId(branchProductDTO.getProductId(), branchProductDTO.getBranchId());

            
            if (optionalBranchProduct.isPresent()) {
                // If the branch product exists, update it
                Optional<Product> optionalProduct = null;
                try{
                    optionalProduct = productService.getById(branchProductDTO.getProductId());
                } catch (Exception e) {
                    System.err.println("Error processing branch product message: " + message);
                    return;
                }
                Product product = optionalProduct.get();
                
                BranchProduct existing = optionalBranchProduct.get();
                existing.setQuantity(branchProductDTO.getQuantity());
                existing.setDiscount(branchProductDTO.getDiscount());
                existing.setBranchId(branchProductDTO.getBranchId());
                existing.setProduct(product);


                // Update other fields as needed
                branchProductService.update(existing);
                System.out.println("Branch product with branch ID " + branchProductDTO.getBranchId() + "and product ID " + branchProductDTO.getProductId() + " updated.");
            } else {
                // If the branch product does not exist, create it
                BranchProduct newBranchProduct = branchProductService.DtoToEntity(branchProductDTO);
                branchProductService.save(newBranchProduct);
                System.out.println("Branch product with branch ID " + branchProductDTO.getBranchId() + "and product ID " + branchProductDTO.getProductId() + " created.");
            }

        } catch (Exception e) {
            // Print an error message if an exception occurs during message processing
            e.printStackTrace();
            System.err.println("Error processing branch product message: " + message);
        }

    }
}
