package com.example.demo.util;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

import com.example.demo.entity.OutBoxEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomJsonSerializer implements Serializer<OutBoxEvent> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public byte[] serialize(String topic, OutBoxEvent data) {

		try {
			if (data == null) {
				System.out.println("Null received at serializing");
				return null;
			}
			return objectMapper.writeValueAsBytes(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
	}

}
