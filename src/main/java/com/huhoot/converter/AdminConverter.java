package com.huhoot.converter;

import java.util.ArrayList;
import java.util.List;

import com.huhoot.dto.HostResponse;
import org.springframework.stereotype.Component;

import com.huhoot.model.Admin;

@Component
public class AdminConverter {
	public HostResponse toDto(Admin entity) {
		HostResponse dto = new HostResponse();
		dto.setId(entity.getId());
		dto.setUsername(entity.getUsername());
		dto.setNonLocked(entity.isNonLocked());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setModifiedDate(entity.getModifiedDate());
		return dto;
	}

	public List<HostResponse> toListDto(List<Admin> entities) {
		List<HostResponse> dtos = new ArrayList<>();

		for (Admin e : entities) {
			dtos.add(toDto(e));
		}

		return dtos;
	}

}
