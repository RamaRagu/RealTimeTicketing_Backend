package com.Ticket.Postgre.CLI;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Customer class represents a ticket-buying customer with priority.
 * Implements Runnable to perform customer activities in a separate thread.
 */
public class Customer implements Runnable, Comparable<Customer> {
    private static final Logger logger = Logger.getLogger(Customer.class.getName());
    private static final AtomicInteger vipCustomerCount = new AtomicInteger(0);
    private static final AtomicInteger regularCustomerCount = new AtomicInteger(0);

    private final TicketPool ticketPool;
    private final int retrieveRate;
    private final int customerPriority; // 1 for VIP, 2 for regular customer

    /**
     * Constructor to initialize the customer with priority and retrieval rate.
     */
    public Customer(TicketPool ticketPool, int retrieveRate, int customerPriority) {
        this.ticketPool = ticketPool;
        this.retrieveRate = retrieveRate;
        this.customerPriority = customerPriority;

        if (customerPriority == 1) {
            vipCustomerCount.incrementAndGet();
        } else {
            regularCustomerCount.incrementAndGet();
        }
    }

    public static int getVipCustomerCount() {
        return vipCustomerCount.get();
    }

    public static int getRegularCustomerCount() {
        return regularCustomerCount.get();
    }

    /**
     * Compares customers based on their priority (VIP has higher priority).
     */
    @Override
    public int compareTo(Customer other) {
        return Integer.compare(this.customerPriority, other.customerPriority);
    }

    /**
     * Run method simulating the customer attempting to buy tickets at specified rates.
     */
    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            int ticketsToPurchase = random.nextInt(retrieveRate) + 1;
            int ticketsSuccessfullyPurchased = 0;

            for (int i = 0; i < ticketsToPurchase; i++) {
                if (!ticketPool.removeTicket()) break;
                ticketsSuccessfullyPurchased++;
            }

            logTicketPurchase(ticketsSuccessfullyPurchased);

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                logger.severe("Customer interrupted while buying tickets: " + e.getMessage());
            }
        }
    }

    /**
     * Logs the ticket purchase info.
     */
    private void logTicketPurchase(int ticketsSuccessfullyPurchased) {
        if (ticketsSuccessfullyPurchased > 0) {
            logger.log(Level.INFO, "{0} customer bought {1} tickets",
                    new Object[]{(customerPriority == 1 ? "VIP" : "Regular"), ticketsSuccessfullyPurchased});
        } else {
            logger.log(Level.INFO, "{0} customer found no tickets available",
                    (customerPriority == 1 ? "VIP" : "Regular"));
        }
    }
}