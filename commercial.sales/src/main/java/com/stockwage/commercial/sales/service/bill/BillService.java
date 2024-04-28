package com.stockwage.commercial.sales.service.bill;

import java.util.List;
import java.util.Optional;

import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.entity.Bill;

public interface BillService {
    Optional<BillDTO> getById(Long id);
    Bill save(BillDTO billDTO);
    boolean delete(Long id);
    Bill update(Long id, BillDTO bill);
    List<BillDTO> getAll();
    List<BillDTO> findByBranchId(Long branchId);
}
