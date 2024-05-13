package com.stockwage.commercial.sales.service.branchproduct;

public interface BranchProductService {
    Boolean updateQuantity(Long productId, Long branchId, Integer newQuantity);
}
