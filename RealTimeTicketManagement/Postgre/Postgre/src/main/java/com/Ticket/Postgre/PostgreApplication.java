package com.Ticket.Postgre;

import com.Ticket.Postgre.CLI.RealTimeTicketingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostgreApplication implements CommandLineRunner {

	private final RealTimeTicketingSystem ticketingSystem;

	// Constructor-based dependency injection
	@Autowired
	public PostgreApplication(RealTimeTicketingSystem ticketingSystem) {
		this.ticketingSystem = ticketingSystem;
	}

	public static void main(String[] args) {
		SpringApplication.run(PostgreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			ticketingSystem.start(null);
		} catch (Exception e) {
			System.err.println("Error occurred while starting the Ticketing System: " + e.getMessage());
			e.printStackTrace();
		}
	}
}