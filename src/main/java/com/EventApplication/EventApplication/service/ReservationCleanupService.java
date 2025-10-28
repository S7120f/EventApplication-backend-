package com.EventApplication.EventApplication.service;

import com.EventApplication.EventApplication.model.ReservationStatus;
import com.EventApplication.EventApplication.repositry.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationCleanupService {

    private final ReservationRepository reservationRepository;

    public ReservationCleanupService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // Kör varje 1 minut
    @Scheduled(fixedRate = 6000)
    public void expiredOldReservations() {
        reservationRepository.findAll().forEach(reservation -> {
            if (reservation.getStatus() == ReservationStatus.ACTIVE && reservation.getReservedUntil().isBefore(LocalDateTime.now())) {
                reservation.setStatus(ReservationStatus.EXPIRED);
                reservationRepository.save(reservation);
                System.out.println("Reservation " + reservation.getId() + " har gått ut.");
            }
        });
    }
}
