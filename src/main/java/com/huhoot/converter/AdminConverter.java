package com.huhoot.converter;

import com.huhoot.admin.host.HostResponse;
import com.huhoot.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminConverter {
    public static HostResponse toHostResponse(Admin entity) {
        HostResponse dto = new HostResponse();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setIsNonLocked(entity.isNonLocked());
        dto.setCreatedDate(entity.getCreatedDate().getTime());
        dto.setModifiedDate(entity.getModifiedDate().getTime());
        return dto;
    }

}
