package com.anoy.load.item.master.pub.sourceitemmasterpub.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_api_source")
public class ItemMasterRequest {
	
	@Id
	@Column(name="ITEM_ID")
	private Integer itemId;
	@Column(name="ITERFACE_NAME")
	private String interfaceName;
	@Column(name="CREATED_DATE")
	private Date createdDate;
	@Column(name="ITEM_STORE")
	private String itemStore;
	@Column(name="ITEM_LOCATION")
	private String itemLocation;
	@Column(name="ITEM_PRICE")
	private Double itemPrice;
	@Column(name="ITEM_STATUS")
	private String itemStatus;
	@Column(name="ITEM_TYPE")
	private String itemType;
}
