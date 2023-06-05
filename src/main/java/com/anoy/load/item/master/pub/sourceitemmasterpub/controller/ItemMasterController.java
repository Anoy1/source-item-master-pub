package com.anoy.load.item.master.pub.sourceitemmasterpub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ControlEntity;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ItemMasterResponse;
import com.anoy.load.item.master.pub.sourceitemmasterpub.repository.ControlTableRespository;
import com.anoy.load.item.master.pub.sourceitemmasterpub.service.ItemMasterService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api")
public class ItemMasterController {
	
	ItemMasterService itemMasterService;
	
	@Autowired
	ItemMasterController(ItemMasterService itemMasterService){
		this.itemMasterService = itemMasterService;
	}
	
	@GetMapping("/itemRecord")
	public ResponseEntity<ItemMasterResponse> processPublisher() throws JsonProcessingException{
		
		itemMasterService.process();
		ItemMasterResponse itemMasterResponse = new ItemMasterResponse();
		itemMasterResponse.setMesssage("DATA SUCCESSFULLY PUSHED");
		itemMasterResponse.setStatus(String.valueOf(HttpStatus.OK.value()));
		return new ResponseEntity<>(itemMasterResponse,HttpStatus.OK);
		
	}

}
