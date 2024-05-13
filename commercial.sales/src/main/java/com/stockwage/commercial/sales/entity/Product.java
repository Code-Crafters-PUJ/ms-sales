package com.stockwage.commercial.sales.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    @Column(name = "saleprice")
    private Double salePrice;

    @NotNull
    @Column(name = "category_id")
    private Integer categoryId;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<BranchProduct> branchProducts = new ArrayList<>();
    
}
