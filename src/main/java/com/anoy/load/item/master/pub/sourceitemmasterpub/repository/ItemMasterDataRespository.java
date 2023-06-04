package com.anoy.load.item.master.pub.sourceitemmasterpub.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ItemMasterRequest;

public interface ItemMasterDataRespository extends JpaRepository<ItemMasterRequest, Integer>{

	List<ItemMasterRequest> findByCreatedDate(Date runnableDate);

}
