package com.Ticket.Postgre.controller;

import com.Ticket.Postgre.model.Config;
import com.Ticket.Postgre.service.ConfigService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;

import com.Ticket.Postgre.CLI.RealTimeTicketingSystem;

/**
 * Controller for managing ticket configurations.
 */
@RestController
@RequestMapping("/api/tickets")
public class ConfigController {

    private final ConfigService configService;
    private final RealTimeTicketingSystem ticketingSystem;

    /**
     * Constructor to initialize the controller with the ConfigService dependency.
     */
    @Autowired
    public ConfigController(ConfigService configService) {
        this.configService = configService;
        this.ticketingSystem = new RealTimeTicketingSystem(configService);
    }

    /**
     * Endpoint to add a new configuration.
     */
    @PostMapping("/addConfig")
    public CompletableFuture<ResponseEntity<Config>> addConfig(@RequestBody Config config) {
        int maxCapacity = config.getMaxCapacity();
        int maxTickets = config.getTotalTickets();
        int releaseRate = config.getReleaseRate();
        int retrieveRate = config.getRetrievalRate();

        System.out.println("maxCapacity: " + maxCapacity);
        System.out.println("maxTickets: " + maxTickets);
        System.out.println("releaseRate: " + releaseRate);
        System.out.println("retrieveRate: " + retrieveRate);

        // Start the ticketing system asynchronously, without blocking the response
        CompletableFuture<Void> ticketingTask = ticketingSystem.runThread(maxTickets, releaseRate, retrieveRate, maxCapacity, "start");

        // Save the configuration
        Config savedConfig = configService.saveConfig(config);

        // Send the response immediately with a 200 OK status
        return CompletableFuture.completedFuture(ResponseEntity.ok(savedConfig));
    }

    /**
     * Endpoint to post the useType (CLI or GUI) of the ticketing system.
     */
    @PostMapping("/useType")
    public ResponseEntity<String> postUseType(@RequestBody String useType) throws JsonProcessingException {
        // Parse the incoming request body to extract 'useType'
        ObjectMapper objectMapper = new ObjectMapper();
        String useTypeValue = objectMapper.readTree(useType).get("useType").asText();
        System.out.println("useType: " + useTypeValue);

        // Trigger the background task, but return the response immediately
        ticketingSystem.start(useTypeValue);

        // Return a quick response to the client while the background task is running
        return ResponseEntity.ok("Processing started in the background");
    }

    /**
     * Endpoint to retrieve a configuration by its ID.
     * Accepts the configuration ID as a path variable and fetches the associated config.
     * @param id The ID of the configuration to retrieve.
     * @return ResponseEntity containing the configuration or a 404 Not Found if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Config> getConfigById(@PathVariable int id) {
        try {
            Config config = configService.getConfigById(id);
            return ResponseEntity.ok(config);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}