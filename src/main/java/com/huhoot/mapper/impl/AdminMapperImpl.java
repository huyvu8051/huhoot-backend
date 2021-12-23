package com.huhoot.mapper.impl;

import com.huhoot.dto.HostAddRequest;
import com.huhoot.admin.host.HostResponse;
import com.huhoot.dto.HostUpdateRequest;
import com.huhoot.mapper.AdminMapper;
import com.huhoot.model.Admin;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-12-02T10:04:03+0700",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (Temurin)"
)
@Primary
@Component("myAdminMapper")
public class AdminMapperImpl implements AdminMapper {

    @Override
    public HostResponse toDto(Admin entity) {
        if (entity == null) {
            return null;
        }

        HostResponse hostResponse = new HostResponse();

        hostResponse.setId(entity.getId());

        hostResponse.setIsNonLocked(entity.isNonLocked());
        hostResponse.setUsername(entity.getUsername());
       if(entity.getCreatedDate() != null){
            hostResponse.setCreatedDate(entity.getCreatedDate().getTime());
       }
        hostResponse.setCreatedBy(entity.getCreatedBy());
        if(entity.getModifiedDate() != null){
            hostResponse.setModifiedDate(entity.getModifiedDate().getTime());
        }
        hostResponse.setModifiedBy(entity.getModifiedBy());

        return hostResponse;
    }

    @Override
    public void update(HostUpdateRequest request, Admin host) {
        if (request == null) {
            return;
        }

        if (request.getIsNonLocked() != null) {
            host.setNonLocked(request.getIsNonLocked());
        }
        host.setId(request.getId());
        if (request.getUsername() != null) {
            host.setUsername(request.getUsername());
        }
    }

    @Override
    public Admin toEntity(HostAddRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Admin.AdminBuilder<?, ?> admin = Admin.builder();

        admin.username( dto.getUsername() );
        if ( dto.getIsNonLocked() != null ) {
            admin.isNonLocked( dto.getIsNonLocked() );
        }

        return admin.build();
    }

}
