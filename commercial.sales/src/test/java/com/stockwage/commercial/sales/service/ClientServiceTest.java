package com.stockwage.commercial.sales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.stockwage.commercial.sales.dto.ClientDTO;
import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.repository.ClientRepository;
import com.stockwage.commercial.sales.service.client.ClientServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testDtoToEntity() {
        // Given
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("John Doe");
        clientDTO.setCard_id("1234567890");
        clientDTO.setContact("123456789");
        clientDTO.setEmail("john@example.com");

        Client client = new Client();
        client.setName("John Doe");
        client.setCard_id("1234567890");
        client.setContact("123456789");
        client.setEmail("john@example.com");

        when(modelMapper.map(clientDTO, Client.class)).thenReturn(client);

        // When
        Client result = clientService.DtoToEntity(clientDTO);

        // Then
        assertNotNull(result);
        assertEquals(client.getName(), result.getName());
        assertEquals(client.getCard_id(), result.getCard_id());
        assertEquals(client.getContact(), result.getContact());
        assertEquals(client.getEmail(), result.getEmail());

        // Verifying modelMapper method invocation
        verify(modelMapper, times(1)).map(clientDTO, Client.class);
    }

    @Test
    public void testSave() {
        // Mocking client data
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setCard_id("123456789");
        client.setContact("1234567890");
        client.setEmail("john@example.com");

        // Stubbing repository methods
        when(clientRepository.existsByCardId(client.getCard_id())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Calling the service method
        Client result = clientService.save(client);

        // Verifying the result
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("123456789", result.getCard_id());
        assertEquals("1234567890", result.getContact());
        assertEquals("john@example.com", result.getEmail());

        // Verifying repository method invocations
        verify(clientRepository, times(1)).existsByCardId(client.getCard_id());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void testGetById() {
        // Mocking client data
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        client.setName("John Doe");

        // Stubbing repository method
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Calling the service method
        Optional<Client> result = clientService.getById(clientId);

        // Verifying the result
        assertTrue(result.isPresent());
        assertEquals(client, result.get());

        // Verifying repository method invocation
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    public void testGetByCardId() {
        // Mocking client data
        String cardId = "123456789";
        Client client = new Client();
        client.setCard_id(cardId);
        client.setName("John Doe");

        // Stubbing repository method
        when(clientRepository.findByCardId(cardId)).thenReturn(Optional.of(client));

        // Calling the service method
        Optional<Client> result = clientService.getByCardId(cardId);

        // Verifying the result
        assertTrue(result.isPresent());
        assertEquals(client, result.get());

        // Verifying repository method invocation
        verify(clientRepository, times(1)).findByCardId(cardId);
    }

    @Test
    public void testDelete() {
        // Mocking client data
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        // Stubbing repository methods
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Calling the service method
        boolean result = clientService.delete(clientId);

        // Verifying the result
        assertTrue(result);

        // Verifying repository method invocation
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    public void testUpdate() {
        // Mocking client data
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");

        // Stubbing repository method
        when(clientRepository.save(client)).thenReturn(client);

        // Calling the service method
        Client result = clientService.update(client);

        // Verifying the result
        assertNotNull(result);
        assertEquals("John Doe", result.getName());

        // Verifying repository method invocation
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void testGetAll() {
        // Mocking client data
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        clients.add(new Client());

        // Stubbing repository method
        when(clientRepository.findAll()).thenReturn(clients);

        // Calling the service method
        List<Client> result = clientService.getAll();

        // Verifying the result
        assertNotNull(result);
        assertEquals(clients.size(), result.size());

        // Verifying repository method invocation
        verify(clientRepository, times(1)).findAll();
    }
}
