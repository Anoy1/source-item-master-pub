package com.anoy.load.item.master.pub.sourceitemmasterpub.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ControlEntity;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ItemMasterRequest;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ControlTableRespository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ItemMasterDataRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ItemMasterService {

	@Autowired
	ControlTableRespository controlTableRespository;
	@Autowired
	ItemMasterDataRespository itemMasterDataRespository;
	@Autowired 
	KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	ObjectMapper objectMapper = new ObjectMapper();
	private static final String TOPIC = "itemmaster";
	
	public void process() throws JsonProcessingException {
		LocalDateTime ld = LocalDateTime.now();
		Date runnableDate = findLastRunDate();
		List<ItemMasterRequest> itemMasterRequest = getItemData(runnableDate);
		
		for(ItemMasterRequest itemMasterEntity : itemMasterRequest)
		kafkaSendMessage(itemMasterEntity,ld);

	}


	private void kafkaSendMessage(ItemMasterRequest itemMasterEntity,LocalDateTime ld ) throws JsonProcessingException {
		itemMasterEntity.setRunTime(ld.toString());
		String msg = objectMapper.writeValueAsString(itemMasterEntity);
		kafkaTemplate.send(TOPIC, msg);
		
	}


	private List<ItemMasterRequest> getItemData(Date runnableDate) {
		return itemMasterDataRespository.findByCreatedDate(runnableDate);
	}


	private Date findLastRunDate() {
		List<ControlEntity> controlEntity = controlTableRespository.findAll();
		List<ControlEntity> itemMasterData= controlEntity.stream().filter(controldata -> controldata.getIterfaceName().equals("ITEM MASTER")).collect(Collectors.toList());;
		Date itemLastRunDate = itemMasterData.get(0).getRunnableDate();
		return itemLastRunDate;
	}

}
