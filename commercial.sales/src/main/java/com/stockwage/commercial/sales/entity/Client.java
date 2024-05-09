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
@Table(name = "client")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String card_id;

    @NotNull
    @Column(name = "contact")
    private String contact;

    @NotNull
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST)
    private List<Bill> bills = new ArrayList<>();
    

}
