package com.stockwage.commercial.sales.service.branchproduct;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.dto.BranchProductDTO;
import com.stockwage.commercial.sales.entity.BranchProduct;
import com.stockwage.commercial.sales.repository.BranchProductRepository;


@Service
public class BranchProductServiceImpl implements BranchProductService{
    
    @Autowired
    private BranchProductRepository branchProductRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BranchProduct DtoToEntity(BranchProductDTO branchProductDTO) {
        return modelMapper.map(branchProductDTO, BranchProduct.class);
    }

    public Boolean updateQuantity(Long productId, Long branchId, Integer newQuantity) {
        BranchProduct branchProduct = branchProductRepository.findByProductIdAndBranchId(productId, branchId);
        if (branchProduct != null) {
            branchProduct.setQuantity(newQuantity);
            branchProductRepository.save(branchProduct);
            return true;
        } 
        return false;
    }

    @Override
    public Optional<BranchProduct> getById(Long id) {
        return branchProductRepository.findById(id);
    }

    @Override
    public BranchProduct save(BranchProduct branchProduct) {
        return branchProductRepository.save(branchProduct);
    }

    @Override
    public boolean delete(Long id) {
        Optional<BranchProduct> branchProductOptional = branchProductRepository.findById(id);
        if (branchProductOptional.isPresent()) {
            branchProductRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public BranchProduct update(BranchProduct branchProduct) {
        return branchProductRepository.save(branchProduct);
    }

    @Override
    public List<BranchProduct> getAll() {
       return branchProductRepository.findAll();
    }

}
