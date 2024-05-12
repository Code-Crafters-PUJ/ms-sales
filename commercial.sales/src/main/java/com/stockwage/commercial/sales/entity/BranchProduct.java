package com.stockwage.commercial.sales.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "branch_has_product")
@Data
public class BranchProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "branch_id")
    private Long branchId;

    @NotNull
    @Column(name = "product_id")
    private Long productId;

    @NotNull
    private Integer quantity;

    private Integer discount = 0;
}
