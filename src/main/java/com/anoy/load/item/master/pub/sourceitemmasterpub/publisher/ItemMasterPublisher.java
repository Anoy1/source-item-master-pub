package com.anoy.load.item.master.pub.sourceitemmasterpub.publisher;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.anoy.load.item.master.pub.sourceitemmasterpub.config.KafkaConfig;
import com.anoy.load.item.master.pub.sourceitemmasterpub.config.ProducerPartition;
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
	@Autowired
	private ProducerPartition producerPartition;
	@Autowired
	KafkaConfig kafkaConfig;
	
	//private static final String TOPIC = "itemtopic";
	
	public Boolean kafkaSendMessage(ItemMasterRequest item,Map<Integer,String> failureItemList) {
		Boolean sent = true;
		try {
		String msg = objectMapper.writeValueAsString(item);
		kafkaTemplate.send(kafkaConfig.getKafkaTopic(), producerPartition.returnPartition(0, 4),"run-time",msg);
		}catch(Exception e) {
			sent = false;
			failureItemList.put(item.getItemId(), e.getMessage());
			log.error(e.getMessage() + " " + item);
		}
		return sent;
	}

}
