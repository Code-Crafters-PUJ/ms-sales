package com.stockwage.commercial.sales.service.paymentmethod;

import java.util.List;

import com.stockwage.commercial.sales.entity.PaymentMethod;

public interface PaymentMethodService {
    PaymentMethod getById(Long id);
    PaymentMethod getByMethod(String method);
    PaymentMethod save(PaymentMethod paymentMethod);
    boolean delete(Long id);
    PaymentMethod update(PaymentMethod paymentMethod);
    List<PaymentMethod> getAll();
}
