package com.huhoot.admin.manage.host;

import com.huhoot.model.Admin;
import org.mapstruct.MappingTarget;

// @Mapper
public interface AdminMapper {

    // @Mapping(source = "nonLocked", target = "isNonLocked")
    HostResponse toDto(Admin entity);

    // @Mapping(source = "isNonLocked", target = "nonLocked")
    // @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(HostUpdateRequest request,@MappingTarget Admin host);

    Admin toEntity(HostAddRequest dto);
}
