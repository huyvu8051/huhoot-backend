package com.huhoot.auditing;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public abstract class AuditableDto {
    private Long createdDate;
    private String createdBy;
    private Long modifiedDate;
    private String modifiedBy;

    public AuditableDto(Long createdDate, String createdBy, Long modifiedDate, String modifiedBy) {
        setCreatedDate(createdDate);
        setModifiedDate(modifiedDate);
        setCreatedBy(createdBy);
        setModifiedBy(modifiedBy);
    }
}
