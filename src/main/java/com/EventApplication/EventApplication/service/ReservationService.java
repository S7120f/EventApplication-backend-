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
        reservation.setReservedUntil(LocalDateTime.now().plusMinutes(15));
        reservation.setStatus(ReservationStatus.ACTIVE);
        System.out.println("detta är min komando1!!!!!" + LocalDateTime.now().plusMinutes(15));

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void expiredOldReservation() {
        List<TicketReservation> expired = reservationRepository
                .findByReservedUntilBeforeAndStatus(LocalDateTime.now(), ReservationStatus.ACTIVE );

        for (TicketReservation r : expired) {
            Event e = eventRepository.findById(r.getId()).orElseThrow();
            e.setTicketAvailable(e.getTicketAvailable() + r.getQuantity());
            eventRepository.save(e);

            r.setStatus(ReservationStatus.EXPIRED);
            reservationRepository.save(r);
        }
    }







}
