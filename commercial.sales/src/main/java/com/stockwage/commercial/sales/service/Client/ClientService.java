package com.stockwage.commercial.sales.service.client;

import java.util.List;
import java.util.Optional;

import com.stockwage.commercial.sales.dto.ClientDTO;
import com.stockwage.commercial.sales.entity.Client;

public interface ClientService {
    Client DtoToEntity(ClientDTO personaDTO);
    Optional<Client> getById(Long id);
    Optional<Client> getByCardId(String cardId);
    Client save(Client client);
    boolean delete(Long id);
    Client update(Client client);
    List<Client> getAll();
}
