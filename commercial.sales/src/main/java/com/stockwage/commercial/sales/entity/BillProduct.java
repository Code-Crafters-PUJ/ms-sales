package com.stockwage.commercial.sales.entity;

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
@Table(name = "bill_has_product")
@Data
public class BillProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;
   
    @Override
    public String toString() {
        return "BillProduct{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", discountPercentage=" + discountPercentage +
                '}';
    }
}