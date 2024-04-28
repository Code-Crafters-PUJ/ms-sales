package com.stockwage.commercial.sales.service.billproduct;

import java.util.List;

import com.stockwage.commercial.sales.dto.BillProductDTO;
import com.stockwage.commercial.sales.entity.BillProduct;

public interface BillProductService {
    BillProduct save(BillProductDTO billProductDTO);
    List<BillProductDTO> getAll();
    List<BillProductDTO> getAllByBill(Long id);
}
