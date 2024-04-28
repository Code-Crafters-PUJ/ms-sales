package com.stockwage.commercial.sales.service.billproduct;

import java.util.List;

import com.stockwage.commercial.sales.dto.BillProductDTO;
import com.stockwage.commercial.sales.entity.Bill;

public interface BillProductService {
    Bill save(BillProductDTO BillProductDTO);
    List<BillProductDTO> getAll();
}
