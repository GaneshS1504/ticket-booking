package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.OutBoxEvent;
import com.example.demo.entity.OutBoxEvent.Status;
import com.example.demo.repository.OutBoxRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OutBoxEventPublisher {

	private final OutBoxRepository outBoxRepository;
	private final KafkaTemplate<String, OutBoxEvent> kafkaTemplate;
	private static final Logger logger = LoggerFactory.getLogger(OutBoxEventPublisher.class);

	@Transactional
	@Scheduled(fixedDelay = 1, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
	public void publishOutBoxEvent() {

		List<OutBoxEvent> events = outBoxRepository.findDueEvents(LocalDateTime.now(), PageRequest.of(0, 10));
		for (OutBoxEvent event : events) {

			String key = String.valueOf(event.getId());

			ProducerRecord<String, OutBoxEvent> producerRecord = new ProducerRecord<String, OutBoxEvent>("booking-transaction",key, event);

			CompletableFuture<org.springframework.kafka.support.SendResult<String, OutBoxEvent>> response = kafkaTemplate
					.send(producerRecord);

			response.whenComplete((result, exception) -> {
				if (exception == null) {
					logger.info(" Message sent to {}, - userId : {} partition : {}, offset : {} ",
							result.getRecordMetadata().topic(), result.getProducerRecord().key(),
							result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

					markAsSent(event.getId());

				} else {
					logger.error("Error occurred while sending message : {} with exception : {} ", event,
							exception.getMessage());

					markAsFailed(event.getId());
				}
			});

		}

	}

	private void markAsFailed(UUID id) {

		outBoxRepository.findById(id).ifPresent(e -> {
			e.setStatus(Status.FAILED);
			outBoxRepository.save(e);
		});
	}

	private void markAsSent(UUID id) {

		outBoxRepository.findById(id).ifPresent(e -> {
			e.setStatus(Status.SENT);
			e.setRunAt(LocalDateTime.now());
			outBoxRepository.save(e);
		});
	}

}
