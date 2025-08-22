package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.example.demo.entity.OutBoxEvent;
import com.example.demo.util.CustomJsonSerializer;

@Configuration
public class KafkaProducerConfiguration {

	@Bean
	public Map<String, Object> kafkaProducerConfig() {

		Map<String, Object> producerConfig = new HashMap<String, Object>();
		producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomJsonSerializer.class.getName());
		producerConfig.put(ProducerConfig.ACKS_CONFIG,"all");
		return producerConfig;
	}

	@Bean
	public ProducerFactory<String, OutBoxEvent> producerFactory() {
		DefaultKafkaProducerFactory<String, OutBoxEvent> factory= new DefaultKafkaProducerFactory<String, OutBoxEvent>(kafkaProducerConfig());
		return factory;
	}

	@Bean
	public KafkaTemplate<String, OutBoxEvent> kafkaTemplate() {
		return new KafkaTemplate<String, OutBoxEvent>(producerFactory());
	}

	@Bean
	public NewTopic transactionTopic() {
		return TopicBuilder.name("booking-transaction")
				.partitions(3)
				.replicas(1)
				.config("cleanup.policy", "compact,delete")
				.config("retention.ms", String.valueOf(3600 * 250))  // 1 hour: how long Kafka retains data
				.config("segment.ms", String.valueOf(5 * 60 * 1000)) // segment roll every 5 mins 
				.config("delete.retention.ms", String.valueOf(3600 * 100)) // 1 hour: tombstone (null-key message) retention
				.config("file.delete.delay.ms", String.valueOf(3600 * 150))  // 1 hour: delay before physical file deletion
				.config("min.cleanable.dirty.ratio", "0.1") // compact more frequently
				.build();
	}
	
}
