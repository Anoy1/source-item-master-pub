package com.anoy.load.item.master.pub.sourceitemmasterpub.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ControlEntity;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ControlTableRespository;

@Service
public class ItemMasterService {

	@Autowired
	ControlTableRespository controlTableRespository;
	
	public void process() {
		Date runnableDate = findLastRunDate();
		System.out.println(runnableDate);
		
		
	}

	private Date findLastRunDate() {
		List<ControlEntity> controlEntity = controlTableRespository.findAll();
		List<ControlEntity> itemMasterData= controlEntity.stream().filter(controldata -> controldata.getIterfaceName().equals("ITEM MASTER")).collect(Collectors.toList());;
		Date itemLastRunDate = itemMasterData.get(0).getRunnableDate();
		return itemLastRunDate;
	}

}
