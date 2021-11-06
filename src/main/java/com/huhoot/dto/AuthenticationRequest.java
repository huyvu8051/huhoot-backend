package com.huhoot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
	@NotNull
	@NotBlank
	@NotEmpty
	@Length(min = 5, max = 15)
	private String username;
	@NotNull
	@NotBlank
	@NotEmpty
	@Length(min = 5, max = 15)
	private String password;
}
