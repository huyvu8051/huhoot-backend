package com.huhoot.dto;

import com.sun.istack.NotNull;

import lombok.Data;
@Data
public class StudentDto {
	@NotNull
	private String username;
	
	private String fullname;
	
	private String password;	
	
}
