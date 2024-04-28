package com.stockwage.commercial.sales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stockwage.commercial.sales.entity.BillProduct;

public interface BillProductRepository extends JpaRepository<BillProduct, Long> {
    @Query("SELECT bp FROM BillProduct bp WHERE bp.bill.id = :billId")
    List<BillProduct> findByBillId(@Param("billId") Long billId);
}

