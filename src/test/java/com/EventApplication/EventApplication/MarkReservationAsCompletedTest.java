package com.EventApplication.EventApplication;

import com.EventApplication.EventApplication.model.Event;
import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import com.EventApplication.EventApplication.service.ReservationService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MarkReservationAsCompletedTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final EventRepository eventRepository = mock(EventRepository.class);
    private final ReservationService reservationService = new ReservationService(reservationRepository, eventRepository);


    @Test
    void testMarkReservationCompleted_ShouldUpdateStatusToCompleted() {
        // Arrange
        Event event = new Event();
        event.setId(1L);
        event.setTicketAvailable(100);

        TicketReservation reservation = new TicketReservation();
        reservation.setId(10L);
        reservation.setEvent(event);
        reservation.setQuantity(2);
        reservation.setStatus(ReservationStatus.ACTIVE);

        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));

        // Act
        TicketReservation result = reservationService.markReservationCompleted(10L);

        // Assert
        assertEquals(ReservationStatus.COMPLETED, result.getStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }


}
