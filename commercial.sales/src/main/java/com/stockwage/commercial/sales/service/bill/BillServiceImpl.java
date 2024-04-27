package com.stockwage.commercial.sales.service.bill;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.repository.BillRepository;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Override
    public Optional<Bill> getById(Long id) {
        return billRepository.findById(id);
    }

    @Override
    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public boolean delete(Long id) {
        Optional<Bill> billOptional = billRepository.findById(id);
        if (billOptional.isPresent()) {
            billRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Bill update(Bill bill) {
        return billRepository.save(bill);
    }
    
    @Override
    public List<Bill> getAll() {
        return billRepository.findAll();
    }
    
    @Override
    public List<Bill> findByBranchId(Long branchId) {
        return billRepository.findByBranchId(branchId);
    }
}
