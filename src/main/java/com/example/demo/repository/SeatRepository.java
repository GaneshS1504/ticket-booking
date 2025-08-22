package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Seat;

import jakarta.persistence.LockModeType;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
		
	Optional<Seat> findBySeatNumber(int seatNumber);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("Select s From Seat s where s.seatNumber = :id")
	Optional<Seat> findSeatForBooking(@Param("id") int id);
}
