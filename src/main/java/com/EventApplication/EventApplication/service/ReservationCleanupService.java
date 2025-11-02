package com.EventApplication.EventApplication.service;

import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationCleanupService {

    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ReservationCleanupService(ReservationRepository reservationRepository, EventRepository eventRepository,  SimpMessagingTemplate messagingTemplate) {
        this.reservationRepository = reservationRepository;
        this.eventRepository = eventRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // Kör varje 1 minut
    @Scheduled(fixedRate = 60000) // körs varje minut
    public void expiredOldReservations() {

        reservationRepository.findAll().forEach(reservation -> {
            // Hoppa över completed/cancelled
            if (reservation.getStatus() == ReservationStatus.COMPLETED || reservation.getStatus() == ReservationStatus.CANCELLED) {
                System.out.println(" Skipping reservation " + reservation.getId() + " (status: " + reservation.getStatus() + ")");
                return;
            }

            // kolla om tiden har gått ut
            if (reservation.getStatus() == ReservationStatus.ACTIVE && reservation.getReservedUntil().isBefore(LocalDateTime.now())) {

                //Lägg tillbaka biljetter till eventet
                var event = reservation.getEvent();
                event.setTicketAvailable(event.getTicketAvailable() + reservation.getQuantity());
                eventRepository.save(event);


                // Uppdatera reservationens status
                reservation.setStatus(ReservationStatus.EXPIRED);
                reservationRepository.save(reservation);

                //Skicka realtidsuppdatering till frontend
                messagingTemplate.convertAndSend("/topic/events", event);

                System.out.println("Reservation " + reservation.getId() + " har gått ut.");
            }
        });
    }
}
