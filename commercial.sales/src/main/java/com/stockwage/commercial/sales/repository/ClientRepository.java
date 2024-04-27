package com.stockwage.commercial.sales.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockwage.commercial.sales.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

    Optional<Client> findByName(String name);
    
} 