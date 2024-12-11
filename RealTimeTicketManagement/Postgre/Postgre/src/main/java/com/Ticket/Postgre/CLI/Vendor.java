package com.Ticket.Postgre.CLI;

import java.util.logging.Level;
import java.util.logging.Logger;

// used runnable class because in tread we can't do multiple inheritance
@SuppressWarnings("ALL")
public class Vendor implements Runnable {
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());
    private final TicketPool pool;
    private final int releaseRate;
    private int ticketsToRelease;

    public Vendor(TicketPool pool, int releaseRate, int ticketsToRelease) {
        this.pool = pool;
        this.releaseRate = releaseRate;
        this.ticketsToRelease = ticketsToRelease;
    }

    @Override
    public void run() {
        while (ticketsToRelease > 0) {
            int ticketsToAdd = Math.min(releaseRate, ticketsToRelease);
            pool.addTickets(ticketsToAdd);
            ticketsToRelease -= ticketsToAdd;
            logger.log(Level.INFO, "Vendor added {0} tickets, remaining: {1}", new Object[]{ticketsToAdd, ticketsToRelease});
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}