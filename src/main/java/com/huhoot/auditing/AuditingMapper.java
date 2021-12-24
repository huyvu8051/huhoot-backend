package com.huhoot.auditing;

import com.huhoot.model.Auditable;

public interface AuditingMapper {
    public <E extends Auditable, D extends AuditableDto> void setAudit(E entity, D dto);
}
