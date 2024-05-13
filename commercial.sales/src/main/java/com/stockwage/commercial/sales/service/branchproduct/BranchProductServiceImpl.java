package com.stockwage.commercial.sales.service.branchproduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.entity.BranchProduct;
import com.stockwage.commercial.sales.repository.BranchProductRepository;


@Service
public class BranchProductServiceImpl implements BranchProductService{
    
        @Autowired
    private BranchProductRepository branchProductRepository;

    public Boolean updateQuantity(Long productId, Long branchId, Integer newQuantity) {
        BranchProduct branchProduct = branchProductRepository.findByProductIdAndBranchId(productId, branchId);
        if (branchProduct != null) {
            branchProduct.setQuantity(newQuantity);
            branchProductRepository.save(branchProduct);
            return true;
        } 
        return false;
    }

}
