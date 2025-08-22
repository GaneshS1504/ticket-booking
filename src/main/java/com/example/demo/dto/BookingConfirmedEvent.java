package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingConfirmedEvent {

	private Long seatId;
	private Long userId;
	private String paymentRef;
	private String eventId;
}
