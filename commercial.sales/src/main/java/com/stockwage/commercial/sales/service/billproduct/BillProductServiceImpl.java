package com.stockwage.commercial.sales.service.billproduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.dto.BillProductDTO;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.BillProduct;
import com.stockwage.commercial.sales.entity.Product;
import com.stockwage.commercial.sales.repository.BillProductRepository;
import com.stockwage.commercial.sales.repository.BillRepository;
import com.stockwage.commercial.sales.repository.ProductRepository;

@Service
public class BillProductServiceImpl implements BillProductService{

    @Autowired
    private BillProductRepository billProductRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public BillProduct save(BillProductDTO billProductDTO) {
        Bill bill = new Bill();
        Product product = new Product();
        Optional<Bill> optionalBill = billRepository.findById(billProductDTO.getBill_id());
        Optional<Product> optionalProduct = productRepository.findById(billProductDTO.getProduct_id());

        if (!optionalBill.isPresent() || !optionalProduct.isPresent()) {
            return null;
        }

        bill = optionalBill.get();
        product = optionalProduct.get();

        BillProduct billProduct = new BillProduct();
        billProduct.setBill(bill);
        billProduct.setQuantity(billProductDTO.getQuantity());
        billProduct.setUnitPrice(product.getSalePrice());
        billProduct.setDiscountPercentage(product.getDiscount());
        billProduct.setProduct(product);
        return billProductRepository.save(billProduct);

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
        System.out.println("Despues del error ?");
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
