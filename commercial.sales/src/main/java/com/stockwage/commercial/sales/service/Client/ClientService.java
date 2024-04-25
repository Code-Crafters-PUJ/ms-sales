package com.stockwage.commercial.sales.service.Client;

import java.util.List;

import com.stockwage.commercial.sales.entity.Client;

public interface ClientService {
    Client getById(Integer id);
    Client getByName(String name);
    Client save(Client client);
    void delete(Integer id);
    Client update(Client client);
    List<Client> getAll();
}
