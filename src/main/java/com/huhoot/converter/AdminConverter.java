package com.huhoot.converter;

import com.huhoot.dto.HostDetailsResponse;
import com.huhoot.dto.HostResponse;
import com.huhoot.dto.PageResponse;
import com.huhoot.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AdminConverter {
    public static HostResponse toHostResponse(Admin entity) {
        HostResponse dto = new HostResponse();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setNonLocked(entity.isNonLocked());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setModifiedDate(entity.getModifiedDate());
        return dto;
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

}
