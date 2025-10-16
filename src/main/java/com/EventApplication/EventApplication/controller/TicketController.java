package com.EventApplication.EventApplication.controller;

import com.EventApplication.EventApplication.dto.TicketCreationResponse;
import com.EventApplication.EventApplication.dto.TicketRequest;
import com.EventApplication.EventApplication.model.Ticket;
import com.EventApplication.EventApplication.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin("*")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PostMapping("/create/{eventId}")
    public TicketCreationResponse createTicket(@PathVariable Long eventId, @RequestBody TicketRequest quantity) {
        return ticketService.createTicket(eventId, quantity.getQuantity());
    }

}
