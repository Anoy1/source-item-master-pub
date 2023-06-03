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
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "control_table")
public class ControlEntity {

	@Id
	@Column(name="ITERFACE_NAME")
	private String iterfaceName;
	@Column(name="RUNNABLE_DATE")
	private Date runnableDate;

	
}
