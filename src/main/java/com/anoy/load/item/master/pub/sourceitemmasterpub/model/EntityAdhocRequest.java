package com.anoy.load.item.master.pub.sourceitemmasterpub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "entity_adhoc")
public class EntityAdhocRequest {
	@Id
	private Integer uniqueId;
	private Integer itemId;
	private String interfaceName;
}
