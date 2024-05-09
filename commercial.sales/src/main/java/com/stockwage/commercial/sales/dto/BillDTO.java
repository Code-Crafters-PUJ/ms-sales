package com.stockwage.commercial.sales.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BillDTO {
    private Long id;
    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String seller;

    private Long branchId;

    private boolean withholdingTax;

    private boolean chargeTax;

    private Long clientId;

    private Long paymentMethodId;
    
}
