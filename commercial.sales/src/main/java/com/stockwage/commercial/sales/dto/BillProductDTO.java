package com.stockwage.commercial.sales.dto;

import lombok.Data;

@Data
public class BillProductDTO {
    private Long product_id;
    private Long bill_id;
    private Integer quantity;
}
