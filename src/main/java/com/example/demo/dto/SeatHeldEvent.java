package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatHeldEvent {

	private int seatId;
	private int userId;
	private LocalDateTime holdUntil;
	private String eventId;
}
