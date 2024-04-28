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
    private Integer quantity;

    @NotNull
    @Column(name = "costprice")
    private Double costPrice;

    @NotNull
    @Column(name = "saleprice")
    private Double salePrice;

    private Integer discount;

    @NotNull
    @Column(name = "category_id")
    private Integer categoryId;
}
