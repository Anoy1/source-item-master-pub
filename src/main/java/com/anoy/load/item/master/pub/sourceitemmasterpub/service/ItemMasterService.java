package com.anoy.load.item.master.pub.sourceitemmasterpub.service;

import java.sql.SQLDataException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.anoy.load.item.master.pub.sourceitemmasterpub.constants.ItemMasterConstants;
import com.anoy.load.item.master.pub.sourceitemmasterpub.exception.ItemMasterDataNotFoundException;
import com.anoy.load.item.master.pub.sourceitemmasterpub.exception.ItemMasterRuntimeNotFoundException;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ControlEntity;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.DashBoard;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.EntityAdhocRequest;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ItemMasterRequest;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.MessageIdEntity;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.PublisherLogEntity;
import com.anoy.load.item.master.pub.sourceitemmasterpub.publisher.ItemMasterPublisher;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ControlTableRespository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.EntityAdhocRepository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ItemMasterDataRespository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.MessageSequenceRepository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.PublisherLogRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
	@Autowired
	PublisherLogRepository publisherLogRepository;
	@Autowired
	ObjectMapper mapper;


	public void process(DashBoard dashBoard) throws JsonProcessingException{
		PublisherLogEntity publisherLogEntity = new PublisherLogEntity();	
		final UUID uniqueKey = UUID.randomUUID();
		try {
		LocalDateTime localDate = LocalDateTime.now();
		publisherLogEntity.setCreatedDate(localDate);
		dashBoard.setCreatedDate(localDate.toString());
		dashBoard.setUniqueKey(uniqueKey.toString());
		ControlEntity controlEntrityLastRundateData = findLastRunDate();
		Date runnableDate = controlEntrityLastRundateData.getRunnableDate();
		log.info(ItemMasterConstants.LAST_RUNDATE + runnableDate);
		Optional<List<ItemMasterRequest>> itemMasterRequest = getItemData(runnableDate,ItemMasterConstants.ITEM_MASTER);
		log.info("Item Master Record as per last Run Date : "+ itemMasterRequest);
		Optional<List<EntityAdhocRequest>> adhocItemMasterRequest = getAdocItemData();
		log.info("Item Master Record as per Adhoc : "+ adhocItemMasterRequest);
		publisherLogEntity.setTotalMessageCount(itemMasterRequest.get().size() + adhocItemMasterRequest.get().size());
		log.info("Total Number Of Records : "+ itemMasterRequest.get().size() + adhocItemMasterRequest.get().size());
		dashBoard.setItemApiCount(itemMasterRequest.get().size());
		dashBoard.setAdhocCount(adhocItemMasterRequest.get().size());
		if(itemMasterRequest.isPresent() || adhocItemMasterRequest.isPresent()) {
			publishMessages(itemMasterRequest,adhocItemMasterRequest,localDate,publisherLogEntity,dashBoard);
			saveLastRunDate(localDate,controlEntrityLastRundateData);
		}
		}catch(ItemMasterRuntimeNotFoundException e) {
		log.error(e.getLocalizedMessage());
		publisherLogEntity.setComment(e.getMessage());
		publisherLogEntity.setStatus(ItemMasterConstants.EXCEPTION);
		dashBoard.setComment(e.getMessage());
		dashBoard.setStatus(ItemMasterConstants.EXCEPTION);
		}
		catch(ItemMasterDataNotFoundException e) {
			log.error(e.getLocalizedMessage());
			publisherLogEntity.setComment(e.getMessage());
			publisherLogEntity.setStatus(ItemMasterConstants.EXCEPTION);
			dashBoard.setComment(e.getMessage());
			dashBoard.setStatus(ItemMasterConstants.EXCEPTION);
		}catch(Exception e) {
			log.error(e.getLocalizedMessage());
			publisherLogEntity.setComment(e.getMessage());
			publisherLogEntity.setStatus(ItemMasterConstants.EXCEPTION);
			dashBoard.setComment(e.getMessage());
			dashBoard.setStatus(ItemMasterConstants.EXCEPTION);
		}
		finally {
			if(ObjectUtils.isEmpty(dashBoard.getStatus()))dashBoard.setStatus(ItemMasterConstants.INCOMPLETE);
			if(ObjectUtils.isEmpty(dashBoard.getComment()))dashBoard.setComment(ItemMasterConstants.INCOMPLETE);
			log.info(mapper.writeValueAsString(dashBoard));
			publisherLogRepository.save(publisherLogEntity);
			log.info(ItemMasterConstants.INTERNAL_SAVE);
		}

	}


	private void saveLastRunDate(LocalDateTime localDate,ControlEntity controlEntrityLastRundateData) {
		controlEntrityLastRundateData.setRunnableDate(Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant()));		
		//controlTableRespository.save(controlEntrityLastRundateData);
	}


	private void publishMessages(Optional<List<ItemMasterRequest>> itemMasterRequest,
			Optional<List<EntityAdhocRequest>> adhocItemMasterRequest, LocalDateTime localDate,PublisherLogEntity publisherLogEntity,DashBoard dashBoard) throws ItemMasterDataNotFoundException{
		try {
		messageSequenceRepository.save(new MessageIdEntity());
		Integer messageId = messageSequenceRepository.findTopByOrderByIdDesc();
		dashBoard.setMessageId(messageId);
		publisherLogEntity.setMessageId(messageId);
		List<ItemMasterRequest> successItemApiList = new ArrayList<ItemMasterRequest>();
		List<EntityAdhocRequest> successAdhocList = new ArrayList<EntityAdhocRequest>();
		Map<Integer,String> failureItemList = new HashMap<Integer,String>();
		if(!itemMasterRequest.get().isEmpty())
		{
			itemMasterRequest.get().stream().forEach(item -> {
				item.setRunTime(localDate.toString());
				item.setMessageId(messageId.toString());
				Boolean sent = itemMasterPublisher.kafkaSendMessage(item,failureItemList);
				if(sent) {
					successItemApiList.add(item);
				}
				
			});
		}
		
		if(!adhocItemMasterRequest.get().isEmpty()) {
			adhocItemMasterRequest.get().stream().forEach(adhocItem ->{
				Optional<ItemMasterRequest> item = itemMasterDataRespository.findById(adhocItem.getItemId());
				if(item.isPresent()){
					item.get().setRunTime(localDate.toString());
					item.get().setMessageId(messageId.toString());
					Boolean sent = itemMasterPublisher.kafkaSendMessage(item.get(),failureItemList);
					if(sent) {
						successAdhocList.add(adhocItem);
					}
				}		
				entityAdhocRepository.delete(adhocItem);
			});
		}
		dataSetToDashboard(failureItemList,successAdhocList,successItemApiList,dashBoard,publisherLogEntity);
	}catch(Exception e) {
		throw new ItemMasterDataNotFoundException("Unable to fetch the Item master Dat from database");
	}
}


	private void dataSetToDashboard(Map<Integer, String> failureItemList, List<EntityAdhocRequest> successAdhocList,
			List<ItemMasterRequest> successItemApiList,DashBoard dashBoard,PublisherLogEntity publisherLogEntity) {
		if(failureItemList.size()>0 && (successAdhocList.size() + successItemApiList.size())>0) {
			dashBoard.setStatus(ItemMasterConstants.PARTIAL_SUCCESS);
			publisherLogEntity.setStatus(ItemMasterConstants.PARTIAL_SUCCESS);
			dashBoard.setComment("Partially Successsful In Transffering to Kafka");
			publisherLogEntity.setComment("Partially Successsful In Transffering to Kafka");
			log.info(String.format("Some of the messages are published from %s successfully : %s \n and from %s successfully : %s", 
					"Item Database on runtime" ,successItemApiList.toString(),"Adhoc", successAdhocList.toString()));
			log.info(String.format("Some of the messages are failed to publish : %s",failureItemList));
		}
		else if(publisherLogEntity.getTotalMessageCount() == (successAdhocList.size() + successItemApiList.size())) {
			dashBoard.setStatus(ItemMasterConstants.SUCCESS);
			publisherLogEntity.setStatus(ItemMasterConstants.SUCCESS);
			dashBoard.setComment("Successsful In Transffering to Kafka");
			publisherLogEntity.setComment("Successsful In Transffering to Kafka");
			log.info(String.format("All the message are published from %s successfully : %s \n and from %s successfully : %s", 
					"Item Database on runtime" ,successItemApiList.toString(),"Adhoc", successAdhocList.toString()));
		}
		else {
			dashBoard.setStatus(ItemMasterConstants.FAILED);
			publisherLogEntity.setStatus(ItemMasterConstants.FAILED);
			dashBoard.setComment("Failure In Transffering to Kafka");
			publisherLogEntity.setComment("Failure In Transffering to Kafka");
			log.info(String.format("All of the messages are failed to publish : %s",failureItemList));
		}
		publisherLogEntity.setSuccessMessageCount(successAdhocList.size() + successItemApiList.size());
		publisherLogEntity.setSuccessMessage(successItemApiList.toString() + successAdhocList.toString());
		publisherLogEntity.setFailureMessageCount(failureItemList.size());
		publisherLogEntity.setFailureMessage(failureItemList.toString());
		dashBoard.setMessage(successItemApiList.toString() + successAdhocList.toString());
		dashBoard.setFailureCount(failureItemList.size());
		dashBoard.setFailureMessage(failureItemList.toString());
		
	}


	private Optional<List<EntityAdhocRequest>> getAdocItemData() {
		return Optional.ofNullable(entityAdhocRepository.getAdhocMessage());
	}



	private Optional<List<ItemMasterRequest>> getItemData(Date runnableDate,String interfaceName) throws ItemMasterDataNotFoundException{
		Optional<List<ItemMasterRequest>> itemMasterList;
		try {
			itemMasterList = Optional.ofNullable(itemMasterDataRespository.findByCreatedDateAndInterfaceName(runnableDate,interfaceName));
			}catch(Exception e) {
				throw new ItemMasterDataNotFoundException("Unable to fetch the Item master Dat from database");
			}
	return 	itemMasterList;
}


	private ControlEntity findLastRunDate() throws ItemMasterRuntimeNotFoundException{
		return controlTableRespository.findById(ItemMasterConstants.ITEM_MASTER).orElseThrow(()-> new ItemMasterRuntimeNotFoundException("Unable to Connect to the last run date Database"));

	}

}
