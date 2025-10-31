package com.EventApplication.EventApplication.controller;

import com.EventApplication.EventApplication.dto.ReservationRequest;
import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import com.EventApplication.EventApplication.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = {
        "http://localhost:4200", //  lokala Angular
        "https://seashell-app-tacu5.ondigitalocean.app" //  deployade Angular-app
})
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ReservationController(ReservationService reservationService, ReservationRepository reservationRepository, SimpMessagingTemplate messagingTemplate) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.messagingTemplate = messagingTemplate;
    }



    // endpoint för att skapa reservation
    @PostMapping
    public ResponseEntity<TicketReservation> reserve(@RequestBody ReservationRequest request) {
        TicketReservation reservation = reservationService.createReservation(request.getEventId(), request.getQuantity());

        // Skicka realtidsuppdatering till alla klienter som lyssnar på eventet
        messagingTemplate.convertAndSend("/topic/events", reservation.getEvent());

        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getReservationStatus(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(reservation -> ResponseEntity.ok(reservation.getStatus().name())) // skicka status t.ex. ACTIVE
                .orElse(ResponseEntity.notFound().build());
    }

    //endpoint för klienten när en reservation avbryts
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        Optional<TicketReservation> opt = reservationRepository.findById(id);

        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TicketReservation reservation = opt.get();

        // om inte redan expired/cancelled
        if (reservation.getStatus() == ReservationStatus.ACTIVE) {
            reservationService.cancelReservation(reservation);
            messagingTemplate.convertAndSend("/topic/events", reservation.getEvent());
        }
        return ResponseEntity.ok().build();
    }


}