package com.stockwage.commercial.sales.service.client;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.stockwage.commercial.sales.dto.ClientDTO;
import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Client DtoToEntity(ClientDTO clientDTO) {
        return modelMapper.map(clientDTO, Client.class);
    }

    @Override
    public Optional<Client> getById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Optional<Client> getByCardId(String cardId) {
        return clientRepository.findByCardId(cardId);
    }

    @Override
    public Client save(Client client) {
        try {
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Client with the same name already exists", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            clientRepository.deleteById(id);
            System.out.println("Client deleted successfully");
            return true;
        } else {
            System.out.println("Client not found");
            return false;
        }
    }
    
    @Override
    public Client update(Client client) {
        try {
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Client with the same card id already exists", e);
        }
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }
    
}
