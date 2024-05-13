package com.stockwage.commercial.sales.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @NotNull
    private Integer quantity;

    private Integer discount = 0;
}
