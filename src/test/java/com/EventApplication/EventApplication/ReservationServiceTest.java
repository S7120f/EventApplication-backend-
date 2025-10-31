package com.EventApplication.EventApplication;

import com.EventApplication.EventApplication.model.Event;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import com.EventApplication.EventApplication.service.ReservationService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final EventRepository eventRepository = mock(EventRepository.class);
    private final ReservationService reservationService = new ReservationService(reservationRepository, eventRepository);

    @Test
    void createReservation_shouldDecreaseTicketsAndSaveReservation(){
        Event event = new Event();
        event.setId(1L);
        event.setTicketAvailable(10);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        TicketReservation reservation = reservationService.createReservation(1L, 2);

        assertEquals(8, event.getTicketAvailable());
        assertEquals(2, reservation.getQuantity());
        assertNotNull(reservation.getReservedUntil());
        verify(eventRepository).save(event);
        verify(reservationRepository).save(reservation);


    }
}
