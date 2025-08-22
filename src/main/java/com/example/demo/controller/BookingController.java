package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Seat;
import com.example.demo.repository.SeatRepository;
import com.example.demo.service.BookingService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/booking")
@AllArgsConstructor
public class BookingController {
	
	private final BookingService bookingService;
	private final SeatRepository seatRepository;
	
	@PostMapping("/hold")
	public ResponseEntity<?> hold(@RequestParam int seatId, @RequestParam int userId) throws JsonProcessingException {
		bookingService.holdSeat(seatId, userId);
	    return ResponseEntity.ok(Map.of("status","HELD", "seatId", seatId, "userId", userId));
	}
	
	 @PostMapping("/seed")
	  public ResponseEntity<?> seed(@RequestParam int seatNumber) {
	    Seat seat = new Seat();
	    seat.setSeatNumber(seatNumber);
	    seat.setStatus(com.example.demo.entity.Status.available);
	    seatRepository.save(seat);
	    return ResponseEntity.ok(seat);
	  }
}
