package com.EventApplication.EventApplication;

import com.EventApplication.EventApplication.model.Event;
import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.model.TicketReservation;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import com.EventApplication.EventApplication.service.ReservationCleanupService;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReservationCleanupServiceTest {

    ReservationRepository reservationRepository = mock(ReservationRepository.class);
    EventRepository eventRepository = mock(EventRepository.class);
    SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);


    @Test
    void expiredOldReservations_shouldExpireActiveReservations() {

        ReservationCleanupService service = new ReservationCleanupService(reservationRepository, eventRepository, messagingTemplate);

        Event event = new Event();
        event.setTicketAvailable(5);

        TicketReservation res = new TicketReservation();
        res.setId(1L);
        res.setEvent(event);
        res.setQuantity(2);
        res.setStatus(ReservationStatus.ACTIVE);
        res.setReservedUntil(LocalDateTime.now().minusMinutes(1));

        when(reservationRepository.findAll()).thenReturn(List.of(res));

        service.expiredOldReservations();

        assertEquals(ReservationStatus.EXPIRED, res.getStatus());
        assertEquals(7, event.getTicketAvailable());
        verify(eventRepository).save(event);
        verify(reservationRepository).save(res);
        verify(messagingTemplate).convertAndSend("/topic/events", event);

    }
}
