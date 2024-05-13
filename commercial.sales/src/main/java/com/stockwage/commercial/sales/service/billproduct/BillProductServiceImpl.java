package com.stockwage.commercial.sales.service.billproduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.dto.BillProductDTO;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.BillProduct;
import com.stockwage.commercial.sales.entity.BranchProduct;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.repository.BillProductRepository;
import com.stockwage.commercial.sales.repository.BillRepository;
import com.stockwage.commercial.sales.repository.BranchProductRepository;
import com.stockwage.commercial.sales.repository.ProductRepository;
import com.stockwage.commercial.sales.service.branchproduct.BranchProductService;

@Service
public class BillProductServiceImpl implements BillProductService{

    @Autowired
    private BillProductRepository billProductRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchProductRepository branchProductRepository;

    @Autowired
    private BranchProductService branchProductService;
    
    @Override
    public BillProduct save(BillProductDTO billProductDTO, Long billId) {
        Bill bill = new Bill();
        Product product = new Product();
        Optional<Bill> optionalBill = billRepository.findById(billId);
        Optional<Product> optionalProduct = productRepository.findById(billProductDTO.getProduct_id());

        if (!optionalBill.isPresent() || !optionalProduct.isPresent()) {
            return null;
        }

        bill = optionalBill.get();
        product = optionalProduct.get();

        BranchProduct branchProduct = branchProductRepository.findByProductIdAndBranchId(product.getId(), bill.getBranchId());
        if (branchProduct == null) {
            return null;
        }
        
        BillProduct billProduct = new BillProduct();

        billProduct.setBill(bill);
        billProduct.setProduct(product);
        billProduct.setQuantity(billProductDTO.getQuantity());
        billProduct.setUnitPrice(product.getSalePrice());
        billProduct.setDiscountPercentage(branchProduct.getDiscount());
        BillProduct newBillProduct = billProductRepository.save(billProduct);
        if( newBillProduct != null) {
            Integer newQuantity = branchProduct.getQuantity() - billProductDTO.getQuantity();
            if(branchProductService.updateQuantity(product.getId(), billRepository.findById(billId).get().getBranchId(), newQuantity)){
                return newBillProduct;   
            }
        }
        return null;
    }

	@Override
    public List<BillProductDTO> getAll() {
        List<BillProduct> billProducts = billProductRepository.findAll();
        List<BillProductDTO> billProductDTOs = new ArrayList<>();
        for (BillProduct billProduct : billProducts) {
            BillProductDTO billProductDTO = new BillProductDTO();
            billProductDTO.setQuantity(billProduct.getQuantity());
            billProductDTO.setDiscountPercentage(billProduct.getDiscountPercentage());
            billProductDTO.setUnitPrice(billProduct.getUnitPrice());
            billProductDTO.setBill_id(billProduct.getBill().getId());
            billProductDTO.setProduct_id(billProduct.getProduct().getId());
            billProductDTOs.add(billProductDTO);
        }
        return billProductDTOs;
    }

    @Override
    public List<BillProductDTO> getAllByBill(Long id) {
        List<BillProduct> billProducts = billProductRepository.findByBillId(id);
        List<BillProductDTO> billProductDTOs = new ArrayList<>();
        for (BillProduct billProduct : billProducts) {
            BillProductDTO billProductDTO = new BillProductDTO();
            billProductDTO.setQuantity(billProduct.getQuantity());
            billProductDTO.setDiscountPercentage(billProduct.getDiscountPercentage());
            billProductDTO.setUnitPrice(billProduct.getUnitPrice());
            billProductDTO.setBill_id(billProduct.getBill().getId());
            billProductDTO.setProduct_id(billProduct.getProduct().getId());
            billProductDTOs.add(billProductDTO);
        }
        return billProductDTOs;
    }
}
