package com.stockwage.commercial.sales.dto;

import lombok.Data;

@Data
public class BranchProductDTO {
    private Long branchId;
    private Long productId;
    private Integer discount;
    private Integer quantity;
}
