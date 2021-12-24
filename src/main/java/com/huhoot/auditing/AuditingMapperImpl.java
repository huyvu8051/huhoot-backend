package com.huhoot.auditing;

import com.huhoot.model.Auditable;
import org.springframework.stereotype.Component;

@Component
public class AuditingMapperImpl implements AuditingMapper{
    @Override
    public <E extends Auditable, D extends AuditableDto> void setAudit(E entity, D dto) {

        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedBy(entity.getModifiedBy());

        if (entity.getCreatedDate() != null) {
            dto.setCreatedDate(entity.getCreatedDate().getTime());
        }

        if (entity.getModifiedDate() != null) {
            dto.setModifiedDate(entity.getModifiedDate().getTime());
        }
    }
}
