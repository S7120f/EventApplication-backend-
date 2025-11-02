package com.EventApplication.EventApplication;

import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import com.EventApplication.EventApplication.service.ReservationService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsReservationCompletedTest {


    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final EventRepository eventRepository = mock(EventRepository.class);
    private final ReservationService reservationService = new ReservationService(reservationRepository, eventRepository);

    @Test
    void testIsReservationCompleted_ShouldReturnTrue_WhenCompleted() {
        TicketReservation reservation = new TicketReservation();
        reservation.setId(5L);
        reservation.setStatus(ReservationStatus.COMPLETED);

        when(reservationRepository.findById(5L)).thenReturn(Optional.of(reservation));

        boolean result = reservationService.isReservationCompleted(5L);

        assertTrue(result);
    }

    @Test
    void testIsReservationCompleted_ShouldReturnFalse_WhenNotCompleted() {
        TicketReservation reservation = new TicketReservation();
        reservation.setId(6L);
        reservation.setStatus(ReservationStatus.ACTIVE);

        when(reservationRepository.findById(6L)).thenReturn(Optional.of(reservation));

        boolean result = reservationService.isReservationCompleted(6L);

        assertFalse(result);
    }

}
