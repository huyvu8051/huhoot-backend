package com.huhoot.converter;

import com.huhoot.dto.HostResponse;
import com.huhoot.model.Admin;
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

}
