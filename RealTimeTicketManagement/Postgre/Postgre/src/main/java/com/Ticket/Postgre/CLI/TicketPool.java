package com.Ticket.Postgre.CLI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketPool {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());
    private final List<Integer> tickets;
    private final int maxCapacity;

    private static int totalTicketsReleased = 0;
    private static int totalTicketsPurchased = 0;

    public TicketPool(int maxCapacity) {
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addTickets(int quantity) {
        while (tickets.size() + quantity > maxCapacity) {
            try {
                logger.log(Level.INFO, "Pool full, vendor waiting to add tickets...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        for (int i = 0; i < quantity; i++) tickets.add(1);
        totalTicketsReleased += quantity;
        logger.log(Level.INFO, "{0} tickets added. Available: {1}", new Object[]{quantity, tickets.size()});
        notifyAll();
    }

    public synchronized boolean removeTicket() {
        while (tickets.isEmpty()) {
            try {
                logger.log(Level.INFO, "No tickets available, customer waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        tickets.remove(0);
        totalTicketsPurchased++;
        logger.log(Level.INFO, "Ticket purchased. Available: {0}", tickets.size());
        notifyAll();
        return true;
    }

    public static int getTotalTicketsReleased() {
        return totalTicketsReleased;
    }

    public static int getTotalTicketsPurchased() {
        return totalTicketsPurchased;
    }
}