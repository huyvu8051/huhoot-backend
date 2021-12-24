package com.huhoot.auditing;

import com.huhoot.model.Auditable;

public abstract class AuditingDtoMapper<E extends Auditable, D extends AuditableDto> {
    protected void setAudit(E entity, D dto) {

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
