package com.huhoot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.huhoot.dto.AuthenticationRequest;
import com.huhoot.dto.AuthenticationResponse;
import com.huhoot.service.impl.MyUserDetailsService;
import com.huhoot.utils.jwtUtil;

@RestController
public class AuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private jwtUtil jwtUtil;

	@PostMapping("/authentication")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception {
		try {
			String formattedUsername = request.getUsername().trim().toLowerCase();
			request.setUsername(formattedUsername);

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (LockedException e) {
			return ResponseEntity.badRequest().body("Account is locked!");
		}catch (Exception e) {
			return ResponseEntity.badRequest().body("Username or password incorrect!");
		}

		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(request.getUsername());

		final String jwt = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
	}
}
