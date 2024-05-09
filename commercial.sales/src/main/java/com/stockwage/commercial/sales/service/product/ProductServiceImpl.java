package com.stockwage.commercial.sales.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }
    
    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    public boolean delete(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    public List<Product> getAll() {
        String message = 1 + "," + 2;
        rabbitTemplate.convertAndSend("update-product-queue", message);
        return productRepository.findAll();
    }

    @Override
    public void updateProductQuantity(Long id, Integer quantity) {

        String message = id + "," + quantity;
        rabbitTemplate.convertAndSend("update-product-queue", message);
        
        productRepository.updateProductQuantity(id, quantity);
    }

}