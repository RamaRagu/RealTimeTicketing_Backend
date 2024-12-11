
package com.Ticket.Postgre.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "release_rate", nullable = false)
    private int releaseRate;

    @Column(name = "retrieval_rate", nullable = false)
    private int retrievalRate;

    @Column(name = "total_tickets", nullable = false)
    private int totalTickets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getReleaseRate() {
        return releaseRate;
    }

    public void setReleaseRate(Integer releaseRate) {
        this.releaseRate = releaseRate;
    }

    public Integer getRetrievalRate() {
        return retrievalRate;
    }

    public void setRetrievalRate(Integer retrievalRate) {
        this.retrievalRate = retrievalRate;
    }

    public Integer getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(Integer totalTickets) {
        this.totalTickets = totalTickets;
    }
}