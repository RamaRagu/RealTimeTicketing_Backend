package com.Ticket.Postgre.CLI;

/**
 * Configuration class to hold system-wide ticketing parameters.
 */
public class Configuration {
    private final int maxTickets;
    private final int releaseRate;
    private final int retrieveRate;
    private final int maxCapacity;

    /**
     * Constructor to initialize ticketing system parameters.
     */
    public Configuration(int maxTickets, int releaseRate, int retrieveRate, int maxCapacity) {
        this.maxTickets = maxTickets;
        this.releaseRate = releaseRate;
        this.retrieveRate = retrieveRate;
        this.maxCapacity = maxCapacity;
    }

    public int getMaxTickets() {
        return maxTickets;
    }

    public int getReleaseRate() {
        return releaseRate;
    }

    public int getRetrieveRate() {
        return retrieveRate;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}