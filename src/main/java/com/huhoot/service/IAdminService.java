package com.huhoot.service;

import java.util.List;

import com.huhoot.dto.AdminDto;

public interface IAdminService {
	List<AdminDto> findAll();
}
