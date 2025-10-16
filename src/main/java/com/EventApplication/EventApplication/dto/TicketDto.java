package com.EventApplication.EventApplication.dto;

public class TicketDto {
    private Long id;
    private Long eventId;
    private boolean sold;
    private boolean reserved;

    public TicketDto(Long id, Long eventId, boolean sold, boolean reserved) {
        this.id = id;
        this.eventId = eventId;
        this.sold = sold;
        this.reserved = reserved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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
