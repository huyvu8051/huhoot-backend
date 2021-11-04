package com.huhoot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AdminDto {
	private String username;
	private boolean isDeleted;
	private Date createdDate;
	private Date modifiedDate;
}
