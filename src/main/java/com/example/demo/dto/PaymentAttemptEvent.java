package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAttemptEvent {

	private Long seatId;
	private Long userId;
	private int attempt; // 1..N
	private LocalDateTime holdUntil;
	private String eventId;
}
