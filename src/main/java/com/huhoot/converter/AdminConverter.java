package com.huhoot.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.huhoot.dto.AdminDto;
import com.huhoot.model.Admin;

@Component
public class AdminConverter {
	public AdminDto toDto(Admin entity) {
		AdminDto dto = new AdminDto();
		dto.setUsername(entity.getUsername());
		dto.setDeleted(entity.isDeleted());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setModifiedDate(entity.getModifiedDate());
		return dto;
	}

	public List<AdminDto> toListDto(List<Admin> entities) {
		List<AdminDto> dtos = new ArrayList<>();

		entities.stream().forEach(e -> {
			dtos.add(toDto(e));
		});

		return dtos;
	}

}
