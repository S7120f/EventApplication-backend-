package com.EventApplication.EventApplication.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Ticket {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private boolean sold = false;
    private boolean reserved = false;

    private LocalDateTime reservedUntil;

    public Ticket(Long id, Event event, boolean sold, boolean reserved) {
        this.id = id;
        this.event = event;
        this.sold = sold;
        this.reserved = reserved;
    }

    public Ticket() {}

    public Ticket(Event event) {
        this.event = event;
    }

    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }
}
