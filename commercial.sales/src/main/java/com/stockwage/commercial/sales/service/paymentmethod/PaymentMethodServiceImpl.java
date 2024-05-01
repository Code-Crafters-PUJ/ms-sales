package com.stockwage.commercial.sales.service.paymentmethod;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.dto.PaymentMethodDTO;
import com.stockwage.commercial.sales.entity.PaymentMethod;
import com.stockwage.commercial.sales.repository.PaymentMethodRepository;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PaymentMethod DtoToEntity(PaymentMethodDTO paymentMethodDTO) {
        return modelMapper.map(paymentMethodDTO, PaymentMethod.class);
    }

    @Override
    public Optional<PaymentMethod> getById(Long id) {
        return paymentMethodRepository.findById(id);
    }

    @Override
    public Optional<PaymentMethod> getByMethod(String method) {
        return paymentMethodRepository.findByMethod(method);
    }

    @Override
    public PaymentMethod save(PaymentMethod paymentMethod) {
        if(paymentMethodRepository.existsByMethod(paymentMethod.getMethod())) {
            throw new DuplicateKeyException("Payment method already exists");
        }
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public boolean delete(Long id) {
        Optional<PaymentMethod> paymentMethodOptional = paymentMethodRepository.findById(id);
        if (paymentMethodOptional.isPresent()) {
            paymentMethodRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PaymentMethod update(PaymentMethod paymentMethod) {
        try {
            return paymentMethodRepository.save(paymentMethod);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Payment method with the same name already exists", e);
        }
    }

    @Override
    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAll();
    }
    
}