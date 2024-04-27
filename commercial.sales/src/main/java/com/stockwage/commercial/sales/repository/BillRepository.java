package com.stockwage.commercial.sales.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stockwage.commercial.sales.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query(value = "SELECT * FROM bill WHERE branch_id = ?1", nativeQuery = true)
    List<Bill> findByBranchId(Long branchId);
}
