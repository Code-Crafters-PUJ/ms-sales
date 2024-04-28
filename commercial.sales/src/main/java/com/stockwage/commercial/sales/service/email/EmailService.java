package com.stockwage.commercial.sales.service.email;

import com.stockwage.commercial.sales.dto.BillDTO;

public interface EmailService {
    public void sendEmail(BillDTO bill);
}
