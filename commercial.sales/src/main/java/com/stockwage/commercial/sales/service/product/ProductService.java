package com.stockwage.commercial.sales.service.product;

import java.util.List;
import java.util.Optional;

import com.stockwage.commercial.sales.dto.ProductDTO;
import com.stockwage.commercial.sales.entity.Product;

public interface ProductService {
    public Product DtoToEntity(ProductDTO productDTO);
    Optional<Product> getById(Long id);
    Product save(Product product);
    boolean delete(Long id);
    Product update(Product product);
    List<Product> getAll();
}
