package com.EventApplication.EventApplication.service;

import com.EventApplication.EventApplication.model.Event;
import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StripeService {

    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;

    public StripeService(ReservationRepository reservationRepository, EventRepository eventRepository) {
        this.reservationRepository = reservationRepository;
        this.eventRepository = eventRepository;
    }

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public String createCheckoutSession(Long reservationId) throws StripeException {
        Stripe.apiKey = stripeApiKey;



        TicketReservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        Event evet = reservation.getEvent();

        // check expired reservation
        if (reservation.getReservedUntil().isBefore(LocalDateTime.now())) {
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservationRepository.save(reservation);
            throw new RuntimeException("Reservation expired, cannot proceed to payment");
        }

        //Create a payment session
        SessionCreateParams params = SessionCreateParams.builder() // using builder to build up complex object
                .setMode(SessionCreateParams.Mode.PAYMENT)// PAYMENT = one-time payment
                .setSuccessUrl("https://stingray-app-fe45r.ondigitalocean.app/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://stingray-app-fe45r.ondigitalocean.app/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder() // adds a product or service to the payment
                                .setQuantity((long) reservation.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("sek")
                                                .setUnitAmount((long) (evet.getPrice() * 100)) // in swedish "ören"
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(evet.getTitle())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()

                )
                .putMetadata("reservationId", reservationId.toString()) // importen look up why
                .build();

        Session session = Session.create(params);
        return session.getUrl(); // send to frontend

    }


}
