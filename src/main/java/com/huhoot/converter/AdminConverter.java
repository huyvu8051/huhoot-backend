package com.huhoot.converter;

import com.huhoot.dto.HostDetailsResponse;
import com.huhoot.dto.HostResponse;
import com.huhoot.dto.PageResponse;
import com.huhoot.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public PageResponse<HostResponse> toListHostRespones(Page<Admin> entities) {
        PageResponse<HostResponse> response = new PageResponse<>();
        for (Admin e : entities) {
            response.getList().add(toDto(e));

        }
        response.setTotalElements(entities.getTotalElements());
        return response;
    }

    public HostDetailsResponse toHostDetailsResponse(Admin entity) {
        HostDetailsResponse hostDetailsResponse = new HostDetailsResponse();
        hostDetailsResponse.setId(entity.getId());
        hostDetailsResponse.setUsername(entity.getUsername());
        hostDetailsResponse.setNonLocked(entity.isNonLocked());
        hostDetailsResponse.setCreatedDate(entity.getCreatedDate());
        hostDetailsResponse.setModifiedDate(entity.getModifiedDate());
        return hostDetailsResponse;
    }

    public List<HostResponse> toListHostResponse(List<Admin> entities) {
        List<HostResponse> responses = new ArrayList<>();

        for (Admin e : entities) {
            responses.add(toDto(e));
        }

        return responses;
    }
}
