package com.EventApplication.EventApplication.dto;

import java.util.List;

public class TicketCreationResponse {
    private Long eventId;
    private int ticketsCreated;
    private int ticketsRemaining;
    private List<TicketDto> tickets;

    public TicketCreationResponse(Long eventId, int ticketsCreated, int ticketsRemaining, List<TicketDto> tickets) {
        this.eventId = eventId;
        this.ticketsCreated = ticketsCreated;
        this.ticketsRemaining = ticketsRemaining;
        this.tickets = tickets;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getTicketsCreated() {
        return ticketsCreated;
    }

    public void setTicketsCreated(int ticketsCreated) {
        this.ticketsCreated = ticketsCreated;
    }

    public int getTicketsRemaining() {
        return ticketsRemaining;
    }

    public void setTicketsRemaining(int ticketsRemaining) {
        this.ticketsRemaining = ticketsRemaining;
    }

    public List<TicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDto> tickets) {
        this.tickets = tickets;
    }
}
