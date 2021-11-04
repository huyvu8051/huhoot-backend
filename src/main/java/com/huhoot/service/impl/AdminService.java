package com.huhoot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huhoot.converter.AdminConverter;
import com.huhoot.dto.AdminDto;
import com.huhoot.model.Admin;
import com.huhoot.repository.IAdminRepository;
import com.huhoot.service.IAdminService;

@Service
public class AdminService implements IAdminService{

	@Autowired
	private IAdminRepository iAdminRepository;
	
	@Autowired
	private AdminConverter adminConverter;
	
	@Override
	public List<AdminDto> findAll() {
		
		List<Admin> admins = iAdminRepository.findAll();
		
		return adminConverter.toListDto(admins);
	}

}
