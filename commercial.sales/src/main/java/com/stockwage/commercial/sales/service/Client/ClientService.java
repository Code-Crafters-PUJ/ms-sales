package com.stockwage.commercial.sales.service.client;

import java.util.List;
import java.util.Optional;

import com.stockwage.commercial.sales.entity.Client;

public interface ClientService {
    Optional<Client> getById(Long id);
    Optional<Client> getByName(String name);
    Client save(Client client);
    boolean delete(Long id);
    Client update(Client client);
    List<Client> getAll();
}
