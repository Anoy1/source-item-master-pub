package com.anoy.load.item.master.pub.sourceitemmasterpub.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ControlEntity;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.EntityAdhocRequest;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ItemMasterRequest;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.MessageIdEntity;
import com.anoy.load.item.master.pub.sourceitemmasterpub.publisher.ItemMasterPublisher;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ControlTableRespository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.EntityAdhocRepository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ItemMasterDataRespository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.MessageSequenceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ItemMasterService {

	@Autowired
	ControlTableRespository controlTableRespository;
	@Autowired
	ItemMasterDataRespository itemMasterDataRespository;
	@Autowired
	EntityAdhocRepository entityAdhocRepository;
	@Autowired
	MessageSequenceRepository messageSequenceRepository;
	@Autowired
	private ItemMasterPublisher itemMasterPublisher;


	public void process() throws JsonProcessingException {
		
		try {
		LocalDateTime localDate = LocalDateTime.now();
		Date runnableDate = findLastRunDate();
		Optional<List<ItemMasterRequest>> itemMasterRequest = getItemData(runnableDate);
		Optional<List<EntityAdhocRequest>> adhocItemMasterRequest = getAdocItemData();
		if(itemMasterRequest.isPresent() || adhocItemMasterRequest.isPresent()) {
		publishMessages(itemMasterRequest,adhocItemMasterRequest,localDate);
		}
		}catch(Exception e) {
			
		}

	}


	private void publishMessages(Optional<List<ItemMasterRequest>> itemMasterRequest,
			Optional<List<EntityAdhocRequest>> adhocItemMasterRequest, LocalDateTime localDate) {
		messageSequenceRepository.save(new MessageIdEntity());
		String messageId = messageSequenceRepository.findTopByOrderByIdDesc().toString();
		if(!itemMasterRequest.get().isEmpty())
		{
			
			itemMasterRequest.get().stream().forEach(item -> {
				item.setRunTime(localDate.toString());
				item.setMessageId(messageId);
				itemMasterPublisher.kafkaSendMessage(item,"Item Database on runtime");
				
			});
		}
		
		if(!adhocItemMasterRequest.get().isEmpty()) {
			adhocItemMasterRequest.get().stream().forEach(adhocItem ->{
				Optional<ItemMasterRequest> item = itemMasterDataRespository.findById(adhocItem.getItemId());
				if(item.isPresent()){
					item.get().setRunTime(localDate.toString());
					item.get().setMessageId(messageId);
					itemMasterPublisher.kafkaSendMessage(item.get(),"Adhoc");	
				}
				entityAdhocRepository.delete(adhocItem);
			});
		}
	}


	private Optional<List<EntityAdhocRequest>> getAdocItemData() {
		return Optional.ofNullable(entityAdhocRepository.getAdhocMessage());
	}



	private Optional<List<ItemMasterRequest>> getItemData(Date runnableDate) {
		return Optional.ofNullable(itemMasterDataRespository.findByCreatedDate(runnableDate));
	}


	private Date findLastRunDate() {
		List<ControlEntity> controlEntity = controlTableRespository.findAll();
		List<ControlEntity> itemMasterData= controlEntity.stream().filter(controldata -> controldata.getIterfaceName().equals("ITEM MASTER")).collect(Collectors.toList());;
		Date itemLastRunDate = itemMasterData.get(0).getRunnableDate();
		return itemLastRunDate;
	}

}
