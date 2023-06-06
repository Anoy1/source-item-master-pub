package com.anoy.load.item.master.pub.sourceitemmasterpub.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "message_sequence")
public class MessageIdEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer messageId;
}
