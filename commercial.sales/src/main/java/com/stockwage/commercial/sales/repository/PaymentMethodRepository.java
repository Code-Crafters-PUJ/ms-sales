package com.stockwage.commercial.sales.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockwage.commercial.sales.entity.PaymentMethod;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>{

    Optional<PaymentMethod> findByMethod(String method);
    Boolean existsByMethod(String method);
    
} 