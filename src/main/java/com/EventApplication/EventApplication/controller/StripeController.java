package com.EventApplication.EventApplication.controller;


import com.EventApplication.EventApplication.dto.CheckoutRequest;
import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.service.StripeService;
import com.stripe.exception.StripeException;
import org.hibernate.annotations.Check;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@CrossOrigin("*")
public class StripeController {


    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout-session")
    public ResponseEntity<Map<String, String>> createCheckout(@RequestBody CheckoutRequest request) {

        ReservationStatus reservationStatus = ReservationStatus.ACTIVE;



        try {
            String checkOutUrl = stripeService.createCheckoutSession(request.getReservationId());
            return ResponseEntity.ok(Map.of(
                    "url", checkOutUrl,
                     "status", reservationStatus.toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
