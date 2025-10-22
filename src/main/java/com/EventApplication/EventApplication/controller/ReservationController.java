package com.EventApplication.EventApplication.controller;

import com.EventApplication.EventApplication.dto.ReservationRequest;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<TicketReservation> reserve(@RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request.getEventId(), request.getQuantity()));
    }
}
