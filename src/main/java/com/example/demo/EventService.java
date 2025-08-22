package com.example.demo;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PaymentAttemptEvent;
import com.example.demo.dto.SeatHeldEvent;
import com.example.demo.entity.OutBoxEvent;
import com.example.demo.repository.OutBoxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EventService {

	private final OutBoxRepository outboxRepo;
	private final ObjectMapper om;

	public void createOutBoxEvent(String type, Object payload) throws JsonProcessingException {

		OutBoxEvent e = new OutBoxEvent();
		e.setId(UUID.randomUUID());
		if (payload instanceof SeatHeldEvent p)
			p.setEventId(e.getId().toString()); // using event id in payload for idempotency
		if (payload instanceof PaymentAttemptEvent p)
			p.setEventId(e.getId().toString());
		e.setPayload(om.writeValueAsString(payload));
		e.setType(type);

		outboxRepo.save(e);
	}

}
