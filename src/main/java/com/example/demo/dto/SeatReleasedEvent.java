package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatReleasedEvent {

	private Long seatId;
	private Long userId;
	private String reason; // "EXPIRED" | "PAYMENT_FAILED"
	private String eventId;
}
