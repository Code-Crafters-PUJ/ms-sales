package com.stockwage.commercial.sales.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stockwage.commercial.sales.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

    @Query("SELECT c FROM Client c WHERE c.card_id = :cardId")
    Optional<Client> findByCardId(String cardId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Client e WHERE e.card_id = :cardId")
    boolean existsByCardId(@Param("cardId") String cardId);
}