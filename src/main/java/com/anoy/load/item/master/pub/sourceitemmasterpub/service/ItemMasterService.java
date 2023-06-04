package com.anoy.load.item.master.pub.sourceitemmasterpub.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ControlEntity;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ItemMasterRequest;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ControlTableRespository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ItemMasterDataRespository;

@Service
public class ItemMasterService {

	@Autowired
	ControlTableRespository controlTableRespository;
	@Autowired
	ItemMasterDataRespository itemMasterDataRespository;
	
	public void process() {
		Date runnableDate = findLastRunDate();
		List<ItemMasterRequest> itemMasterRequest = getItemData(runnableDate);
		System.out.println(itemMasterRequest.get(0).getItemId());
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
