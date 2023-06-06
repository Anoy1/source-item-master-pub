package com.anoy.load.item.master.pub.sourceitemmasterpub.publisher;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ItemMasterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemMasterPublisher {
	
	@Autowired
	ObjectMapper objectMapper = new ObjectMapper();
	@Autowired 
	KafkaTemplate<String, String> kafkaTemplate;
	
	private static final String TOPIC = "itemmaster";
	
	public void kafkaSendMessage(ItemMasterRequest item, String source) {
		try {
		String msg = objectMapper.writeValueAsString(item);
		kafkaTemplate.send(TOPIC, msg);
		log.info(String.format("The message is published from %s successfully : %s", source,msg));
		}catch(Exception e) {
			log.error(e.getMessage());
		}
	}

}
