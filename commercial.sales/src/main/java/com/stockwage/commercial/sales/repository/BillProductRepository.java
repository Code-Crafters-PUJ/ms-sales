package com.stockwage.commercial.sales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stockwage.commercial.sales.entity.BillProduct;

public interface BillProductRepository extends JpaRepository<BillProduct, Long>{

    @Query(value = "SELECT * FROM bill_has_product WHERE bill_id = ?1", nativeQuery = true)
    List<BillProduct> findByBillId(Long billId);
    
}
