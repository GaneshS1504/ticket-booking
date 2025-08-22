package com.example.demo.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.EventService;
import com.example.demo.dto.PaymentAttemptEvent;
import com.example.demo.dto.SeatHeldEvent;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Status;
import com.example.demo.repository.SeatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class BookingService {

	private final SeatRepository seatRepo;
	private final EventService eventService;
	
	@Value("${booking.payment.holdMinutes}")
	private int holdMinutes;

	private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

	public BookingService(SeatRepository seatRepo, EventService eventService) {
		super();
		this.seatRepo = seatRepo;
		this.eventService = eventService;
	}

	@org.springframework.transaction.annotation.Transactional
	public void holdSeat(int seatId, int userId) throws JsonProcessingException {

		Seat seat = seatRepo.findSeatForBooking(seatId)
				.orElseThrow(() -> new IllegalArgumentException("Seat not found"));

		if (seat.getStatus() == Status.booked) {
			throw new IllegalStateException("Seat already booked");
		}

		if (seat.getStatus() == Status.held && seat.getHoldByUntill() != null
				&& seat.getHoldByUntill().isAfter(LocalDateTime.now()) && seat.getUserId() == userId) {

			logger.info("Seat is on Hold make payment");
			eventService.createOutBoxEvent("AttemtPayment",
					new PaymentAttemptEvent(seat.getUserId(), seat.getSeatNumber(),1, seat.getHoldByUntill(), null));

		}else {
			throw new RuntimeException("Seat is on Hold");
		}

		seat.setStatus(Status.held);
		seat.setUserId(userId);
		seat.setHoldByUntill(LocalDateTime.now().plusMinutes(holdMinutes));
		seatRepo.save(seat);

		eventService.createOutBoxEvent("SeatHeld",
				new SeatHeldEvent(seat.getUserId(), seat.getSeatNumber(), seat.getHoldByUntill(), null));

	}

}
