package com.stockwage.commercial.sales.service.bill;

import java.util.List;
import java.util.Optional;

import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.Client;

public interface BillService {
    Optional<BillDTO> getById(Long id);
    BillDTO save(BillDTO billDTO);
    boolean delete(Long id);
    Bill update(Long id, BillDTO bill);
    List<BillDTO> getAll();
    List<BillDTO> findByBranchId(Long branchId);
    void generatePDF(Long billId);
    void generatePDF(Bill bill, Client client);
}
