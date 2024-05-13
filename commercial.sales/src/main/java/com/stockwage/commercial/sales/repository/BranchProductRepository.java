package com.stockwage.commercial.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stockwage.commercial.sales.entity.BranchProduct;

public interface BranchProductRepository extends JpaRepository<BranchProduct, Long> {
    BranchProduct findByProductIdAndBranchId(Long productId, Long branchId);
}
