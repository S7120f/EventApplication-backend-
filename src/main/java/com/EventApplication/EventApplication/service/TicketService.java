package com.EventApplication.EventApplication.service;

import com.EventApplication.EventApplication.dto.TicketCreationResponse;
import com.EventApplication.EventApplication.dto.TicketDto;
import com.EventApplication.EventApplication.model.Event;
import com.EventApplication.EventApplication.model.Ticket;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.EventApplication.EventApplication.repositry.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;


    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
    }

    public TicketCreationResponse createTicket (Long eventId, int quantity) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event with id " + eventId + "not fond"));
        System.out.println("mitt egna print ------" + event);

        List<Ticket> tickets = new ArrayList<>();

        // create how many tickets user want to buy to specific event
        for (int i = 0; i < quantity; i++){
            Ticket ticket = new Ticket(event);
            tickets.add(ticketRepository.save(ticket));
        }

        //update available  tickets of that event
        event.setTicketAvailable(event.getTicketAvailable() - quantity);
        eventRepository.save(event);

        //convert Ticket -> TicketDto
        List<TicketDto> ticketsDtos = tickets.stream()
                .map(t -> new TicketDto(t.getId(), t.getEvent().getId(), t.isSold(), t.isReserved()))
                .collect(Collectors.toList());

        return new TicketCreationResponse(
                event.getId(),
                quantity,
                event.getTicketAvailable(),
                ticketsDtos
        );



    }


}
