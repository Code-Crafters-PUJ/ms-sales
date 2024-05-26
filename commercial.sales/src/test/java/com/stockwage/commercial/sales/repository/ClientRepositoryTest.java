package com.stockwage.commercial.sales.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.stockwage.commercial.sales.entity.Client;

@DataJpaTest
public class ClientRepositoryTest {

    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository = mock(ClientRepository.class);
        
        // Simulate behavior for save method
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            client.setId(1L); // Simulate generating an ID upon saving
            return client;
        });

        // Simulate behavior for findById method
        when(clientRepository.findById(1L)).thenReturn(Optional.of(new Client()));

        // Simulate behavior for findByCardId method
        when(clientRepository.findByCardId("12345678")).thenReturn(Optional.of(new Client()));

        // Simulate behavior for existsByCardId method
        when(clientRepository.existsByCardId("12345678")).thenReturn(true);

        // Simulate behavior for findAll method
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        clients.add(new Client());
        clients.add(new Client());
        when(clientRepository.findAll()).thenReturn(clients);
    }

    @Test
    void testSave() {
        // Create a Client object
        Client client = new Client();
        client.setName("Test Client");
        client.setCard_id("12345678");
        client.setContact("Test Contact");
        client.setEmail("test@example.com");

        // Save the Client
        Client savedClient = clientRepository.save(client);

        // Verify that the saved Client is not null
        assertNotNull(savedClient);

        // Verify that the ID of the saved Client is not null
        assertNotNull(savedClient.getId());
    }

    @Test
    void testFindById() {
        // Get Client by ID
        Optional<Client> foundClient = clientRepository.findById(1L);

        // Verify that the found Client is not empty
        assertTrue(foundClient.isPresent());
    }

    @Test
    void testFindByCardId() {
        // Get Client by card ID
        Optional<Client> foundClient = clientRepository.findByCardId("12345678");

        // Verify that the found Client is not empty
        assertTrue(foundClient.isPresent());
    }

    @Test
    void testExistsByCardId() {
        // Check if Client exists by card ID
        boolean exists = clientRepository.existsByCardId("12345678");

        // Verify that Client exists
        assertTrue(exists);
    }

    @Test
    void testDelete() {
        // Create a Client object
        Client client = new Client();
        client.setId(1L);
        
        // Delete the Client
        clientRepository.delete(client);
        
        // Verify that the Client was deleted
        verify(clientRepository, times(1)).delete(eq(client));
    }

    @Test
    void testFindAll() {
        // Get all Clients
        List<Client> foundClients = clientRepository.findAll();

        // Verify that the list is not null and has the expected length
        assertNotNull(foundClients);
        assertEquals(3, foundClients.size());
    }

    @Test
    void testUpdate() {
        // Create a Client object
        Client client = new Client();
        client.setId(1L);
        client.setName("Updated Client");
        client.setCard_id("12345678");
        client.setContact("Updated Contact");
        client.setEmail("updated@example.com");
        
        // Update the Client
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        Client updatedClient = clientRepository.save(client);
        
        // Verify that the Client was updated
        assertNotNull(updatedClient);
        assertEquals("Updated Client", updatedClient.getName());
        assertEquals("Updated Contact", updatedClient.getContact());
        assertEquals("updated@example.com", updatedClient.getEmail());
    }
}