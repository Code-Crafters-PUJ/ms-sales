package com.stockwage.commercial.sales.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillProductDTO {
    private Long product_id;
    private Long bill_id;
    @NotNull
    private Integer quantity;

    @NotNull
    private Double unitPrice;

    @NotNull
    private Integer discountPercentage;
}
