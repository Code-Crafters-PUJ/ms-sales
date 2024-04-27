package com.stockwage.commercial.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockwage.commercial.sales.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
      
    
}
