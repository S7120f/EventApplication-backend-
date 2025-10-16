package com.EventApplication.EventApplication.repositry;

import com.EventApplication.EventApplication.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {


}
