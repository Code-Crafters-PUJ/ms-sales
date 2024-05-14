package com.stockwage.commercial.sales.service.branchproduct;

import java.util.List;
import java.util.Optional;

import com.stockwage.commercial.sales.dto.BranchProductDTO;
import com.stockwage.commercial.sales.entity.BranchProduct;

public interface BranchProductService {
    public BranchProduct DtoToEntity(BranchProductDTO branchProductDTO);
    Optional<BranchProduct> getById(Long id);
    BranchProduct save(BranchProduct branchProduct);
    boolean delete(Long id);
    BranchProduct update(BranchProduct branchProduct);
    List<BranchProduct> getAll();
    Boolean updateQuantity(Long productId, Long branchId, Integer newQuantity);
    Optional<BranchProduct> findByProductIdAndBranchId(Long productId, Long branchId);
}
