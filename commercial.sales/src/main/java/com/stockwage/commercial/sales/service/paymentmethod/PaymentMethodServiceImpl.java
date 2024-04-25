package com.stockwage.commercial.sales.service.paymentmethod;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.entity.PaymentMethod;
import com.stockwage.commercial.sales.repository.PaymentMethodRepository;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public PaymentMethod getById(Long id) {
        return paymentMethodRepository.findById(id).orElse(null);
    }

    @Override
    public PaymentMethod getByMethod(String method) {
        return paymentMethodRepository.findByMethod(method);
    }

    @Override
    public PaymentMethod save(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public boolean delete(Long id) {
        try {
            paymentMethodRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public PaymentMethod update(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAll();
    }

    
}
