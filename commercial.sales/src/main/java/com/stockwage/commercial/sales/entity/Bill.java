package com.stockwage.commercial.sales.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bill")
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private BillTypeEnum type;

    @NotNull
    @Column(name = "date")
    private LocalDate date;

    @NotNull
    @Column(name = "contact")
    private String contact;

    @NotNull
    @Column(name = "seller")
    private String seller;

    @NotNull
    @Column(name = "branch_id")
    private Long branchId;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "subtotal")
    private Double subtotal;

    @NotNull
    @Column(name = "discount")
    private Double discount;

    @NotNull
    @Column(name = "taxes")
    private Double taxes;

    @NotNull
    @Column(name = "aiu")
    private boolean aiu;

    @NotNull
    @Column(name = "withholding_tax")
    private boolean withholdingTax;

    @NotNull
    @Column(name = "charge_tax")
    private boolean chargeTax;

    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "paymentmethod_id")
    private PaymentMethod paymentMethod;
    
    @JsonIgnore
    @OneToMany(mappedBy = "bill")
    private List<BillProduct> billProducts;
    
}
