package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Status;
import com.example.demo.repository.SeatRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SeatLockTimeOutHandlerService {

	private final SeatRepository seatRepository;

	@Scheduled(fixedDelay = 13000, initialDelay = 1000, timeUnit = TimeUnit.MILLISECONDS)
	@Transactional
	public void checkExpiredLock() {

		seatRepository.findSeatByStatusAndExpirtTime(Status.held, LocalDateTime.now()).ifPresentOrElse(lockedSeats -> {
			lockedSeats.forEach(seat -> {
				seat.setStatus(Status.available);
				seat.setUserId(0);
				seat.setHoldByUntill(null);

				seatRepository.save(seat);
			});
		}, () -> System.out.println("No locked seats found to process."));

	}
}
