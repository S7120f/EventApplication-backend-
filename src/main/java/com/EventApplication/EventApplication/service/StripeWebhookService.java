package com.EventApplication.EventApplication.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StripeWebhookService {

    private final ReservationService reservationService;
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;


    public StripeWebhookService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Transactional
    public void handleWebhookEvent(String payload, String sigHeader) throws SignatureVerificationException {  // (payload) = JSON-data (payment information), (sigHeader) = verifies that Stripe sent the event
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

        if ("checkout.session.completed".equals(event.getType())) {  // filter our events, searching for checkout.session.completed
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

            if (dataObjectDeserializer.getObject().isPresent()) {
                Session session = (Session) dataObjectDeserializer.getObject().get();
                Long reservationId = Long.valueOf(session.getMetadata().get("reservationId"));

                reservationService.markReservationCompleted(reservationId);
                System.out.println("Payment completed for reservation " + reservationId);

            }
        }
    }
}
