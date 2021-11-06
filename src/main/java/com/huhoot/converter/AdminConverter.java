package com.huhoot.converter;

import java.util.ArrayList;
import java.util.List;

import com.huhoot.dto.AdminDTO;
import org.springframework.stereotype.Component;

import com.huhoot.model.Admin;

@Component
public class AdminConverter {
	public AdminDTO toDto(Admin entity) {
		AdminDTO dto = new AdminDTO();
		dto.setId(entity.getId());
		dto.setUsername(entity.getUsername());
		dto.setNonLocked(entity.isNonLocked());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setModifiedDate(entity.getModifiedDate());
		return dto;
	}

	public List<AdminDTO> toListDto(List<Admin> entities) {
		List<AdminDTO> dtos = new ArrayList<>();

		for (Admin e : entities) {
			dtos.add(toDto(e));
		}

		return dtos;
	}

}
