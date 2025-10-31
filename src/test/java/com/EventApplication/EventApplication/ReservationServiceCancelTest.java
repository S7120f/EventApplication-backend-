package com.EventApplication.EventApplication;

import com.EventApplication.EventApplication.model.Event;
import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import com.EventApplication.EventApplication.service.ReservationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReservationServiceCancelTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final EventRepository eventRepository = mock(EventRepository.class);
    private final ReservationService reservationService = new ReservationService(reservationRepository, eventRepository);

    @Test
    void cancelReservation_shouldIncreaseTicketsAndSetCancelled() {
        Event event = new Event();
        event.setTicketAvailable(5);

        TicketReservation reservation = new TicketReservation();
        reservation.setEvent(event);
        reservation.setQuantity(2);

        reservationService.cancelReservation(reservation);

        assertEquals(7, event.getTicketAvailable());
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        verify(eventRepository).save(event);
        verify(reservationRepository).save(reservation);
    }

}
