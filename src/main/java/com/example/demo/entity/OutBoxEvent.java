package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_outbox_event")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OutBoxEvent {

	public enum Status {
		NEW, SENT, FAILED
	}

	@Id
	private UUID id;

	@Column(nullable = false)
	private String type;

	@Lob
	@Column(nullable = false)
	private String payload;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status = Status.NEW;

	@Column(nullable = false)
	private int attempts = 0;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime runAt = LocalDateTime.now();

}
