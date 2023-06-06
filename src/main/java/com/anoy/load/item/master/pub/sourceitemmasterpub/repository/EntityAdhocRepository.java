package com.anoy.load.item.master.pub.sourceitemmasterpub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.anoy.load.item.master.pub.sourceitemmasterpub.constants.ItemMasterConstants;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.EntityAdhocRequest;
import com.anoy.load.item.master.pub.sourceitemmasterpub.model.ItemMasterRequest;

@Repository
public interface EntityAdhocRepository extends JpaRepository<EntityAdhocRequest, Integer>{

	@Query( nativeQuery = true, value=ItemMasterConstants.ADHOC_QUERY)
	public List<EntityAdhocRequest> getAdhocMessage();

}
