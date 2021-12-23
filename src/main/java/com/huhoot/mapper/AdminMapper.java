package com.huhoot.mapper;

import com.huhoot.dto.HostAddRequest;
import com.huhoot.admin.host.HostResponse;
import com.huhoot.dto.HostUpdateRequest;
import com.huhoot.model.Admin;
import org.mapstruct.*;

@Mapper
public interface AdminMapper {

    // @Mapping(source = "nonLocked", target = "isNonLocked")
    HostResponse toDto(Admin entity);

    // @Mapping(source = "isNonLocked", target = "nonLocked")
    // @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(HostUpdateRequest request,@MappingTarget Admin host);

    Admin toEntity(HostAddRequest dto);
}
