package com.huhoot.mapper.impl;

import com.huhoot.admin.manage.host.HostAddRequest;
import com.huhoot.admin.manage.host.HostResponse;
import com.huhoot.admin.manage.host.HostUpdateRequest;
import com.huhoot.auditing.AuditingMapper;
import com.huhoot.mapper.AdminMapper;
import com.huhoot.model.Admin;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AdminMapperImpl implements AdminMapper {


    private final AuditingMapper auditingMapper;

    @Override
    public HostResponse toDto(Admin entity) {
        if (entity == null) {
            return null;
        }

        HostResponse hostResponse = new HostResponse();

        auditingMapper.setAudit(entity,hostResponse);

        hostResponse.setId(entity.getId());

        hostResponse.setIsNonLocked(entity.isNonLocked());
        hostResponse.setUsername(entity.getUsername());
        hostResponse.setCreatedBy(entity.getCreatedBy());

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
