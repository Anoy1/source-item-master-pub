package com.anoy.load.item.master.pub.sourceitemmasterpub.model;

import java.time.LocalDateTime;
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
@Table(name = "item_master_publisher_log")
public class PublisherLogEntity {
@Id
@Column(name = "MESSAGE_ID")
private Integer messageId;
@Column(name = "STATUS")
private String status;
@Column(name = "TOTAL_MESSAGE_COUNT")
private Integer totalMessageCount;
@Column(name = "SUCCESS_MESSAGE_COUNT")
private Integer successMessageCount;
@Column(name = "SUCCESS_MESSAGE")
private String successMessage;
@Column(name = "FAILURE_MESSAGE_COUNT")
private Integer failureMessageCount;
@Column(name = "FAILURE_MESSAGE")
private String failureMessage;
@Column(name = "COMMENT")
private String comment;
@Column(name = "CREATED_DATE")
private LocalDateTime createdDate;

}
