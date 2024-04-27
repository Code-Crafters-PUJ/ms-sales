package com.stockwage.commercial.sales.service.bill;

import java.util.List;
import java.util.Optional;

import com.stockwage.commercial.sales.entity.Bill;

public interface BillService {
    Optional<Bill> getById(Long id);
    Optional<Bill> getByName(String name);
    Bill save(Bill bill);
    boolean delete(Long id);
    Bill update(Bill bill);
    List<Bill> getAll();
}
