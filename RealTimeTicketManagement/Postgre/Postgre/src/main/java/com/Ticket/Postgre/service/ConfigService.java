package com.Ticket.Postgre.service;

import com.Ticket.Postgre.Repo.ConfigRepo;
import com.Ticket.Postgre.model.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class to handle ticket configurations.
 */
@Service
public class ConfigService {

    private final ConfigRepo configRepo;

    /**
     * Constructor to initialize the service with the ConfigRepo dependency.
     * @param configRepo The repository to interact with the database for configurations.
     */
    @Autowired
    public ConfigService(ConfigRepo configRepo) {
        this.configRepo = configRepo;
    }

    /**
     * Save a configuration in the database.
     * @param config The configuration data to be saved.
     * @return The saved configuration object.
     */
    public Config saveConfig(Config config) {
        return configRepo.save(config);
    }

    /**
     * Retrieve a configuration by its ID.
     * @param id The ID of the configuration to retrieve.
     * @return The configuration object.
     */
    public Config getConfigById(int id) {
        return configRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
    }
}