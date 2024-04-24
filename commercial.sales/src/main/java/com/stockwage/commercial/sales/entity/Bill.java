package com.stockwage.commercial.sales.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BillType type;

    @NotNull
    private LocalDate date;

    @NotNull
    private String description;

    @NotNull
    private String seller;

    private String email;

    @NotNull
    private boolean aiu;

    @NotNull
    private boolean withholdingTax;

    @NotNull
    private boolean chargeTax;

    public enum BillType {
        T,
        E
    }
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "paymentMethod_id")
    private PaymentMethod paymentMethod;

    
}
