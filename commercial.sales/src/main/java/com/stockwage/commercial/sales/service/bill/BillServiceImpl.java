package com.stockwage.commercial.sales.service.bill;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.dto.BillDTO;
import com.stockwage.commercial.sales.entity.Bill;
import com.stockwage.commercial.sales.entity.BillTypeEnum;
import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.entity.PaymentMethod;
import com.stockwage.commercial.sales.repository.BillRepository;
import com.stockwage.commercial.sales.repository.ClientRepository;
import com.stockwage.commercial.sales.repository.PaymentMethodRepository;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public Optional<BillDTO> getById(Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            BillDTO billDTO = convertToDTO(bill.get());
            return Optional.of(billDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Bill save(BillDTO billDTO) {
        Bill bill = new Bill();
        Optional<Client> optClient = clientRepository.findById(billDTO.getClientId());
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findById(billDTO.getPaymentMethodId());
        BillTypeEnum billType = null;

        if (!optClient.isPresent() || !optionalPaymentMethod.isPresent()) {
            return null;
        }
        
        try {
            billType = BillTypeEnum.valueOf(billDTO.getType());
        } catch (IllegalArgumentException e) {
            return null;
        }

        Client client = optClient.get();
        PaymentMethod paymentMethod = optionalPaymentMethod.get();

        bill = Bill.builder()
            .type(billType)
            .date(billDTO.getDate())
            .description(billDTO.getDescription())
            .seller(billDTO.getSeller())
            .branchId(billDTO.getBranchId())
            .email(billDTO.getEmail())
            .subtotal(billDTO.getSubtotal())
            .discount(billDTO.getDiscount())
            .taxes(billDTO.getTaxes())
            .aiu(billDTO.isAiu())
            .withholdingTax(billDTO.isWithholdingTax())
            .chargeTax(billDTO.isChargeTax())
            .client(client)
            .paymentMethod(paymentMethod) 
            .build();

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
    public Bill update(Long id, BillDTO billDTO) {
        Optional<Client> optClient = clientRepository.findById(billDTO.getClientId());
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findById(billDTO.getPaymentMethodId());
        BillTypeEnum billType = null;
        try {
            billType = BillTypeEnum.valueOf(billDTO.getType());
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (!optClient.isPresent() || !optionalPaymentMethod.isPresent()) {
            return null;
        }
        Optional<Bill> existingBillOptional = billRepository.findById(id);

        if (existingBillOptional.isPresent()) {
            Bill existingBill = existingBillOptional.get();
            existingBill.setType(billType);
            existingBill.setDate(billDTO.getDate());
            existingBill.setDescription(billDTO.getDescription());
            existingBill.setSeller(billDTO.getSeller());
            existingBill.setBranchId(billDTO.getBranchId());
            existingBill.setEmail(billDTO.getEmail());
            existingBill.setSubtotal(billDTO.getSubtotal());
            existingBill.setDiscount(billDTO.getDiscount());
            existingBill.setTaxes(billDTO.getTaxes());
            existingBill.setAiu(billDTO.isAiu());
            existingBill.setWithholdingTax(billDTO.isWithholdingTax());
            existingBill.setChargeTax(billDTO.isChargeTax());
            existingBill.setClient(optClient.get());
            existingBill.setPaymentMethod(optionalPaymentMethod.get());
            return billRepository.save(existingBill);
        } else {
            return null;
        }
    }
    
    
    
    @Override
    public List<BillDTO> getAll() {
        List<Bill> bills = billRepository.findAll();
        List<BillDTO> billDTOs = new ArrayList<>();
        for (Bill bill : bills) {
            BillDTO billDTO = convertToDTO(bill);
            billDTOs.add(billDTO);
        }
        return billDTOs;
    }
    
    @Override
    public List<BillDTO> findByBranchId(Long branchId) {
        List<Bill> bills = billRepository.findByBranchId(branchId);
        List<BillDTO> billDTOs = new ArrayList<>();
        for (Bill bill : bills) {
            BillDTO billDTO = convertToDTO(bill);
            billDTOs.add(billDTO);
        }
        return billDTOs;
    }

    private BillDTO convertToDTO(Bill bill) {
        BillDTO billDTO = new BillDTO();
        String type = bill.getType().name();
        billDTO.setType(type);
        billDTO.setDate(bill.getDate());
        billDTO.setDescription(bill.getDescription());
        billDTO.setSeller(bill.getSeller());
        billDTO.setBranchId(bill.getBranchId());
        billDTO.setEmail(bill.getEmail());
        billDTO.setSubtotal(bill.getSubtotal());
        billDTO.setDiscount(bill.getDiscount());
        billDTO.setTaxes(bill.getTaxes());
        billDTO.setAiu(bill.isAiu());
        billDTO.setWithholdingTax(bill.isWithholdingTax());
        billDTO.setChargeTax(bill.isChargeTax());
        billDTO.setClientId(bill.getClient().getId());
        billDTO.setPaymentMethodId(bill.getPaymentMethod().getId());
        return billDTO;
    }


}
