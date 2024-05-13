package com.stockwage.commercial.sales.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.dto.ProductDTO;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Product DtoToEntity(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

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
        return productRepository.findAll();
    }
}