package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SeatHeldEvent;
import com.example.demo.entity.OutBoxEvent;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Status;
import com.example.demo.repository.OutBoxRepository;
import com.example.demo.repository.SeatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BookingService {

	private final SeatRepository seatRepo;
	private final OutBoxRepository outboxRepo;
	private final ObjectMapper om;
	
	@Value("${booking.payment.holdMinutes}")
	private int holdMinutes;
	
	public BookingService(SeatRepository seatRepo, OutBoxRepository outboxRepo, ObjectMapper om) {
		super();
		this.seatRepo = seatRepo;
		this.outboxRepo = outboxRepo;
		this.om = om;
	}

	@org.springframework.transaction.annotation.Transactional
	public void holdSeat(int seatId, int userId) throws JsonProcessingException {

		Seat seat = seatRepo.findSeatForBooking(seatId)
				.orElseThrow(() -> new IllegalArgumentException("Seat not found"));

		if (seat.getStatus() == Status.booked) {
			throw new IllegalStateException("Seat already booked");
		}

		if (seat.getStatus() == Status.held && seat.getHoldByUntill() != null
				&& seat.getHoldByUntill().isAfter(LocalDateTime.now())) {
			throw new IllegalStateException("Seat already on hold");
		}

		seat.setStatus(Status.held);
		seat.setUserId(userId);
		seat.setHoldByUntill(LocalDateTime.now().plusMinutes(holdMinutes));
		seatRepo.save(seat);

		createOutBoxEvent("SeatHeld",
				new SeatHeldEvent(seat.getUserId(), seat.getSeatNumber(), seat.getHoldByUntill(), null));
	}
	
	private void createOutBoxEvent(String type, SeatHeldEvent seatHeldEvent) throws JsonProcessingException {
		
		OutBoxEvent e = new OutBoxEvent();
		e.setId(UUID.randomUUID());
		e.setPayload(om.writeValueAsString(seatHeldEvent));
		e.setType(type);
		
		outboxRepo.save(e);
	}
	
}
