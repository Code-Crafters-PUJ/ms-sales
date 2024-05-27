package com.stockwage.commercial.sales.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.stockwage.commercial.sales.dto.ClientDTO;
import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.service.client.ClientService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClientControllerUnitTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllClients() {
        // Arrange
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("John Doe");
        Client client2 = new Client();
        client2.setId(2L);
        client2.setName("Jane Doe");
        when(clientService.getAll()).thenReturn(Arrays.asList(client1, client2));

        // Act
        ResponseEntity<List<Client>> response = clientController.getAllClients();

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(clientService, times(1)).getAll();
    }

    @Test
    public void testAddClient_NullClient() {
        // Act
        ResponseEntity<Client> response = clientController.addClient(null);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddClient_SuccessfulAddition() {
        // Arrange
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("New Client");
        clientDTO.setCard_id("123456");
        clientDTO.setContact("1234567890");
        clientDTO.setEmail("newclient@example.com");

        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setCard_id(clientDTO.getCard_id());
        client.setContact(clientDTO.getContact());
        client.setEmail(clientDTO.getEmail());

        when(clientService.DtoToEntity(clientDTO)).thenReturn(client);
        when(clientService.save(client)).thenReturn(client);

        // Act
        ResponseEntity<Client> response = clientController.addClient(clientDTO);

        // Assert
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(clientService, times(1)).save(client);
    }

    @Test
    public void testGetClientById_InvalidId() {
        // Act
        ResponseEntity<Client> response = clientController.getClientById(null);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetClientById_NotFound() {
        // Arrange
        when(clientService.getById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Client> response = clientController.getClientById(1L);

        // Assert
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetClientById_Found() {
        // Arrange
        Client client = new Client();
        client.setId(1L);
        client.setName("Existing Client");
        when(clientService.getById(1L)).thenReturn(Optional.of(client));

        // Act
        ResponseEntity<Client> response = clientController.getClientById(1L);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Existing Client", response.getBody().getName());
    }

    @Test
    public void testUpdateClient_NotFound() {
        // Arrange
        when(clientService.getById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Client> response = clientController.updateClient(1L, new ClientDTO());

        // Assert
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteClient_InvalidId() {
        // Act
        ResponseEntity<Void> response = clientController.deleteClient(null);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteClient_NotFound() {
        // Arrange
        when(clientService.delete(1L)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = clientController.deleteClient(1L);

        // Assert
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteClient_SuccessfulDeletion() {
        // Arrange
        when(clientService.delete(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = clientController.deleteClient(1L);

        // Assert
        assertEquals(NO_CONTENT, response.getStatusCode());
    }
}
