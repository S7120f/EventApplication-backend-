package com.EventApplication.EventApplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String title;
    private String description;
    private double price;
    private Integer ticketAvailable;

    public Event(Long id, String title, String description, double price, Integer ticketAvailable) {
        Id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.ticketAvailable = ticketAvailable;
    }

    public Event() {

    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getTicketAvailable() {
        return ticketAvailable;
    }

    public void setTicketAvailable(Integer ticketAvailable) {
        this.ticketAvailable = ticketAvailable;
    }
}
