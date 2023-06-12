package com.anoy.load.item.master.pub.sourceitemmasterpub.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Getter
@Setter
@ConfigurationProperties("kafka-config")
@Slf4j
public class KafkaConfig {
	
	private String kafkaTopic;
	private String kafkaHost;
	
	@Bean
	public ProducerFactory<String, String> producerFactory(){
		Map<String,Object> kafkaConfig = new HashMap<>();
		kafkaConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
		kafkaConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
		kafkaConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
		kafkaConfig.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "262144");
		kafkaConfig.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, ProducerPartition.class.getName());
		kafkaConfig.put(ProducerConfig.ACKS_CONFIG, "1");
		return new DefaultKafkaProducerFactory<>(kafkaConfig);
	}
	
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	

}
