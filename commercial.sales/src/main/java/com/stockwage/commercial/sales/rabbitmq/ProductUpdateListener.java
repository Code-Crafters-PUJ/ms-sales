package com.stockwage.commercial.sales.rabbitmq;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.service.product.ProductService;

@Component
public class ProductUpdateListener {

    @Autowired
    ProductService productService;

    @RabbitListener(queues = "update-product-queue")
    public void processProductUpdate(@Payload String message) {
        String[] parts = message.split(",");
        Long productId = Long.parseLong(parts[0]);
        String productName = parts[1];
        String productDescription = parts[2];
        Integer productQuantity = Integer.parseInt(parts[3]);
        Double productCostPrice = Double.parseDouble(parts[4]);
        Double productSalePrice = Double.parseDouble(parts[5]);
        Integer productDiscount = Integer.parseInt(parts[6]);
        Integer productCategoryId = Integer.parseInt(parts[7]);

        Optional<Product> existingProduct = productService.getById(productId);
        if (existingProduct.isPresent()) {
            Product product = new Product();
            product.setId(productId);
            product.setName(productName);
            product.setDescription(productDescription);
            product.setQuantity(productQuantity);
            product.setCostPrice(productCostPrice);
            product.setSalePrice(productSalePrice);
            product.setDiscount(productDiscount);
            product.setCategoryId(productCategoryId);
            Product updatedProduct = productService.update(product);
            System.out.println(updatedProduct.toString());
        }
    }
}