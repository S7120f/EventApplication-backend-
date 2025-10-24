package com.EventApplication.EventApplication.repositry;

import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.model.TicketReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository <TicketReservation, Long> {

    List<TicketReservation> findByReservedUntilBeforeAndStatus(LocalDateTime time, ReservationStatus status);

}
