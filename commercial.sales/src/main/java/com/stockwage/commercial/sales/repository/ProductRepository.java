package com.stockwage.commercial.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stockwage.commercial.sales.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.id = :id")
    int updateProductQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);
}
