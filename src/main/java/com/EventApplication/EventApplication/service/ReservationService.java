package com.EventApplication.EventApplication.service;

import com.EventApplication.EventApplication.model.Event;
import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;

    public ReservationService(ReservationRepository reservationRepository, EventRepository eventRepository) {
        this.reservationRepository = reservationRepository;
        this.eventRepository = eventRepository;
    }


    @Transactional
    public TicketReservation createReservation(Long eventId, int quantity) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event with id " + "not found"));


        //Control for available ticket
        if (event.getTicketAvailable() < quantity) {
            throw new RuntimeException("Not enough tickets available");
        }


        // decrease available tickets
        event.setTicketAvailable(event.getTicketAvailable() - quantity);
        eventRepository.save(event);

        //Create reservation
        TicketReservation reservation = new TicketReservation();
        reservation.setEvent(event);
        reservation.setQuantity(quantity);
        reservation.setReservedUntil(LocalDateTime.now().plusMinutes(1));
        reservation.setStatus(ReservationStatus.ACTIVE);
        System.out.println("detta är min komando1!!!!!" + LocalDateTime.now().plusMinutes(1));

        return reservationRepository.save(reservation);
    }

    public TicketReservation markReservationCompleted(Long reservationId){
        TicketReservation ticketReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("reservation with id " + reservationId + " was not found"));

        ticketReservation.setStatus(ReservationStatus.COMPLETED);
        reservationRepository.save(ticketReservation);

        return ticketReservation;
    }


    public void cancelReservation(TicketReservation reservation) {
        Event event = reservation.getEvent();

        event.setTicketAvailable(event.getTicketAvailable() + reservation.getQuantity());
        eventRepository.save(event);

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
}
