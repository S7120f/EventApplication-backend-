package com.EventApplication.EventApplication.controller;

import com.EventApplication.EventApplication.service.StripeWebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/stripe")
@CrossOrigin(origins = {
        "http://localhost:4200", //  lokala Angular
        "https://stingray-app-fe45r.ondigitalocean.app" //  deployade Angular-app
})
public class StripeWebhookController {

    private final StripeWebhookService stripeWebhookService;

    public StripeWebhookController(StripeWebhookService stripeWebhookService) {
        this.stripeWebhookService = stripeWebhookService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            stripeWebhookService.handleWebhookEvent(payload, sigHeader);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
