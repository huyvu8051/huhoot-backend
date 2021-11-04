package com.huhoot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huhoot.service.IAdminService;

@RestController
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private IAdminService adminService;
	
	@GetMapping("/host")
	public ResponseEntity<?> getAllHost() {
		try {
			return ResponseEntity.ok(adminService.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);
		}
	}
}
