package com.EventApplication.EventApplication.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TicketReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event; // connection to our event-table


    private int quantity;
    private LocalDateTime reservedUntil;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    public TicketReservation(int quantity, LocalDateTime reservedUntil, ReservationStatus status) {
        this.quantity = quantity;
        this.reservedUntil = reservedUntil;
        this.status = status;
    }

    public TicketReservation() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
