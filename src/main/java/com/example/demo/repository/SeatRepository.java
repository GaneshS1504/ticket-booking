package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Seat;
import com.example.demo.entity.Status;

import jakarta.persistence.LockModeType;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
		
	Optional<Seat> findBySeatNumber(int seatNumber);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("Select s From Seat s where s.seatNumber = :id")
	Optional<Seat> findSeatForBooking(@Param("id") int id);
	
	@Query("Select s From Seat s where s.status = :status and "
			+ "s.holdByUntill < :now")
	Optional<List<Seat>> findSeatByStatusAndExpirtTime(@Param("status") Status status, 
			@Param("now") LocalDateTime now);
}
