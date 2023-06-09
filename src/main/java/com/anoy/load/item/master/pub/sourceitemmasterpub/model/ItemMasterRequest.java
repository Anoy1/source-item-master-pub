package com.anoy.load.item.master.pub.sourceitemmasterpub.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	@Column(name="INTERFACE_NAME")
	private String interfaceName;
	@Column(name="CREATED_DATE")
	private Date createdDate;
	@Transient
	private String messageId;
	@Transient
	private String runTime;
}
