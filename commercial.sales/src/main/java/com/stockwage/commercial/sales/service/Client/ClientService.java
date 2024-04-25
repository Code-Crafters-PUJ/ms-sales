package com.stockwage.commercial.sales.service.Client;

import java.util.List;

import com.stockwage.commercial.sales.entity.Client;

public interface ClientService {
    Client getById(Long id);
    Client getByName(String name);
    Client save(Client client);
    boolean delete(Long id);
    Client update(Client client);
    List<Client> getAll();
}
