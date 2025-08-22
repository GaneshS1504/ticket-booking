package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAttemptEvent {

	private int userId;
	private int seatId;
	private int attempt; // 1..N
	private LocalDateTime holdUntil;
	private String eventId;
}
