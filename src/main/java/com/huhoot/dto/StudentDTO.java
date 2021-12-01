package com.huhoot.dto;

import com.sun.istack.NotNull;
import lombok.Data;
@Data
public class StudentDTO {
	@NotNull
	private String username;
	
	private String fullname;
	
	private String password;	
	
}
