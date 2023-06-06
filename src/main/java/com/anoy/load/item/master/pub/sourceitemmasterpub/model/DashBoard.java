package com.anoy.load.item.master.pub.sourceitemmasterpub.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashBoard {
	
private String uniqueKey;
private Integer messageId;
private String message;
private String status;
private String comment;
private String createdDate;
private Integer adhocCount;
private Integer itemApiCount;
private Integer failureCount;
private String failureMessage;

}
