package com.EventApplication.EventApplication.controller;


import com.EventApplication.EventApplication.dto.CheckoutRequest;
import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.service.ReservationService;
import com.EventApplication.EventApplication.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@CrossOrigin(origins = {
        "http://localhost:4200", //  lokala Angular
        "https://stingray-app-fe45r.ondigitalocean.app/" //  deployade Angular-app
})
public class StripeController {


    private final StripeService stripeService;
    private final ReservationService reservationService;

    public StripeController(StripeService stripeService, ReservationService reservationService) {
        this.stripeService = stripeService;
        this.reservationService = reservationService;
    }

    @Value("${stripe.api.key}")
    private String stripeApiKey;


    // Endpoint för att vår stipe checkout
    @PostMapping("/checkout-session")
    public ResponseEntity<Map<String, String>> createCheckout(@RequestBody CheckoutRequest request) {
        ReservationStatus reservationStatus = ReservationStatus.COMPLETED;

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


    // Stripe endpoint för att verifiera betalning
    @GetMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestParam("session_id") String sessionId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        Session session = Session.retrieve(sessionId);

        System.out.println(" Stripe session ID: " + sessionId);
        System.out.println(" Payment status: " + session.getPaymentStatus());
        System.out.println(" Session JSON: " + session.toJson());

        if ("paid".equals(session.getPaymentStatus())) {
            Long reservationId = Long.valueOf(session.getMetadata().get("reservationId"));
            reservationService.markReservationCompleted(reservationId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "reservationId", session.getMetadata().get("reservationId")
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed"
            ));
        }
    }
}
