package com.Ticket.Postgre.CLI;

import com.Ticket.Postgre.model.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;

import com.Ticket.Postgre.service.ConfigService;

/**
 * Main service class to start and manage the ticketing system in real-time.
 */
@Component
public class RealTimeTicketingSystem {

    private static final Logger logger = Logger.getLogger(RealTimeTicketingSystem.class.getName());
    private static final AtomicBoolean systemRunning = new AtomicBoolean(true);
    private static final String LOG_FILE_PATH = "ticketing_system.log";

    private final ConfigService configService;

    static {
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE_PATH, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            logger.severe("Failed to set up FileHandler for logging: " + e.getMessage());
        }
    }

    public RealTimeTicketingSystem(ConfigService configService) {
        this.configService = configService;
    }

    @Async
    public CompletableFuture<String> start(String useType) {
        CompletableFuture<String> futureResponse = new CompletableFuture<>();

        // Simulating async execution, use a separate thread for this
        new Thread(() -> {
            System.out.println("Welcome to the Ticketing System. Please configure the system first.");

            if (useType == null) {
                System.out.println("Waiting for command from the Frontend...");
            }

            if ("CLI".equalsIgnoreCase(useType)) {
                CLIstart();
                futureResponse.complete("CLI");  // Completes the future with a success response.
            } else if ("GUI".equalsIgnoreCase(useType)) {
                System.out.println("Navigate to GUI page");
                futureResponse.complete("GUI");  // Completes the future for GUI option.
            } else {
                futureResponse.complete("Invalid useType");  // Handle invalid useType.
            }
        }).start();  // Start the thread to simulate async execution.

        return futureResponse;
    }

    public void CLIstart() {

        Scanner scanner = new Scanner(System.in);

        int maxCapacity = getValidIntInput(scanner, "Enter max capacity of tickets: ");
        int releaseRate = getValidIntInput(scanner, "Enter ticket release rate: ");
        int retrieveRate = getValidIntInput(scanner, "Enter ticket retrieval rate: ");
        int maxTickets = getValidIntInput(scanner, "Enter total number of tickets: ");

        logInputsToLogFile(maxCapacity, releaseRate, retrieveRate, maxTickets);
        runThread(maxTickets, releaseRate, retrieveRate, maxCapacity, "start");
        scanner.close();
    }

    @Async
    public CompletableFuture<Void> runThread(int maxTickets, int releaseRate, int retrieveRate, int maxCapacity, String command) {
        logInputsToLogFile(maxCapacity, releaseRate, retrieveRate, maxTickets);

        CompletableFuture<String> futureResponse = new CompletableFuture<>();

        Scanner scanner = new Scanner(System.in);
        Configuration systemConfig = new Configuration(maxTickets, releaseRate, retrieveRate, maxCapacity);
        TicketPool ticketPool = new TicketPool(systemConfig.getMaxCapacity());

        Thread vendor1 = null, vendor2 = null;

        System.out.println("System configured. Use commands to start, stop, or check status.");

        Config config = new Config();
        config.setMaxCapacity(maxCapacity);
        config.setReleaseRate(releaseRate);
        config.setRetrievalRate(retrieveRate);
        config.setTotalTickets(maxTickets);

        Config savedConfig = configService.saveConfig(config);

        // Step 2: Command loop
        while (systemRunning.get()) {
            while (command.isEmpty()) {
                System.out.println("Command cannot be empty. Please enter a command (start, stop, status):");
                command = scanner.nextLine().trim().toLowerCase();
            }

            switch (command) {
                case "start":
                    if (vendor1 != null && vendor1.isAlive() && vendor2 != null && vendor2.isAlive()) {
                        System.out.println("System is already running!");
                        command = "";
                        break;
                    }

                    System.out.println("Starting ticketing system simulation...");

                    // Start vendor threads
                    vendor1 = new Thread(new Vendor(ticketPool, systemConfig.getReleaseRate(), systemConfig.getMaxTickets()));
                    vendor2 = new Thread(new Vendor(ticketPool, systemConfig.getReleaseRate(), systemConfig.getMaxTickets()));
                    vendor1.start();
                    vendor2.start();

                    // Set up customer queue
                    PriorityBlockingQueue<Customer> customerQueue = new PriorityBlockingQueue<>();
                    customerQueue.add(new Customer(ticketPool, systemConfig.getRetrieveRate(), 1)); // VIP customer
                    customerQueue.add(new Customer(ticketPool, systemConfig.getRetrieveRate(), 2)); // Regular customer

                    // Start customer threads
                    while (!customerQueue.isEmpty()) {
                        Customer customer = customerQueue.poll();
                        if (customer != null) {
                            new Thread(customer).start();
                        }
                    }

                    logger.info("Ticketing system started.");
                    break;

                case "stop":
                    if (vendor1 == null || vendor2 == null || !vendor1.isAlive() || !vendor2.isAlive()) {
                        System.out.println("System stopped!");
                        command = "";
                        break;
                    }

                    System.out.println("Stopping the real-time ticketing system simulation...");
                    stopSystem(vendor1, vendor2, systemConfig);
                    break;

                case "status":
                    System.out.printf("Current Configuration: Max Capacity: %d, Release Rate: %d, Retrieve Rate: %d, Max Tickets: %d%n",
                            systemConfig.getMaxCapacity(),
                            systemConfig.getReleaseRate(),
                            systemConfig.getRetrieveRate(),
                            systemConfig.getMaxTickets());
                    command = "";
                    break;

                default:
                    System.out.println("Invalid command. Try again.");
                    command = "";
            }
        }

        scanner.close();
        return CompletableFuture.completedFuture(null); // Completing the asynchronous task
    }

    private void stopSystem(Thread vendor1, Thread vendor2, Configuration systemConfig) {
        systemRunning.set(false);
        if (vendor1 != null) vendor1.interrupt();
        if (vendor2 != null) vendor2.interrupt();
        logger.info("Stopping the ticketing system...");
    }

    private void logInputsToLogFile(int maxCapacity, int releaseRate, int retrieveRate, int maxTickets) {
        String message = String.format(
                "Inputs: Max Capacity: %d, Release Rate: %d, Retrieve Rate: %d, Max Tickets: %d",
                maxCapacity, releaseRate, retrieveRate, maxTickets
        );
        logger.info(message);
    }

    private int getValidIntInput(Scanner scanner, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input <= 0) {
                    System.out.println("Please enter a positive integer.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return input;
    }
}