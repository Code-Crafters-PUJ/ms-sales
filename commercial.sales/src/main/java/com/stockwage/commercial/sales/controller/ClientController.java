package com.stockwage.commercial.sales.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockwage.commercial.sales.entity.Client;
import com.stockwage.commercial.sales.service.client.ClientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
@Api(tags = "Client Management", description = "Endpoints for managing clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/all")
    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients")
    public List<Client> getAllClients() {
        return clientService.getAll();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new client", description = "Adds a new client to the system")
    @ApiResponse(responseCode = "201", description = "Client added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Client newClient = clientService.save(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get a client by ID", description = "Retrieves a client by its ID")
    @ApiResponse(responseCode = "200", description = "Client retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientService.getById(id);
        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getByName/{name}")
    @Operation(summary = "Get a client by name", description = "Retrieves a client by its name")
    @ApiResponse(responseCode = "200", description = "Client retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<Client> getClientByName(@PathVariable String name) {
        Optional<Client> client = clientService.getByName(name);
        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update an existing client", description = "Updates an existing client")
    @ApiResponse(responseCode = "200", description = "Client updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied or Bad request")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        if (id == null || updatedClient == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Client> existingClientOptional = clientService.getById(id);
        if (existingClientOptional.isPresent()) {
            Client existingClient = existingClientOptional.get();
            existingClient.setName(updatedClient.getName());
            Client savedClient = clientService.update(existingClient);
            return new ResponseEntity<>(savedClient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a client by ID", description = "Deletes a client by its ID")
    @ApiResponse(responseCode = "200", description = "Client deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean deleted = clientService.delete(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
