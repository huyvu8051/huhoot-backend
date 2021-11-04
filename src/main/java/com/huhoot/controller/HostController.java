package com.huhoot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("host")
public class HostController {
	
	@GetMapping("/challenge")
	public ResponseEntity<?> getAllHost() {
		try {
			return ResponseEntity.ok("this is host challenges");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);
		}
	}
}
