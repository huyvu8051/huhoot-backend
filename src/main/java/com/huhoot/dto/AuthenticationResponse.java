package com.huhoot.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AuthenticationResponse {
	@Getter
	private final String jwt;
	@Getter
	private final String username;
	@Getter
	private final Collection<? extends GrantedAuthority> authorities;

}
