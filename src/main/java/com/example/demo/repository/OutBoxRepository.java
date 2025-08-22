package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.OutBoxEvent;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBoxEvent, UUID> {
	
	@Query("Select o from OutBoxEvent o where "
			+ "o.status = 'NEW' and o.runAt <= :now "
			+ "order by o.createdAt")
	List<OutBoxEvent> findDueEvents(@Param("now") LocalDateTime now, Pageable pageable);
}
