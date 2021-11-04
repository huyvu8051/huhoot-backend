package com.huhoot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("student")
public class StudentController {

	@GetMapping("/challenge")
	public ResponseEntity<?> getAllHost() {
		try {
			return ResponseEntity.ok("this is student challenges");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);
		}
	}
}
