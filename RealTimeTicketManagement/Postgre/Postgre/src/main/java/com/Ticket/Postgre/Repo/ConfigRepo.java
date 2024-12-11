package com.Ticket.Postgre.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Ticket.Postgre.model.Config;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ConfigRepo extends JpaRepository<Config, Integer> {
}