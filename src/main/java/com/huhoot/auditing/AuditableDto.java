package com.huhoot.auditing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AuditableDto {
    private Long createdDate;
    private String createdBy;
    private Long modifiedDate;
    private String modifiedBy;

    public AuditableDto(Date createdDate, String createdBy, Date modifiedDate, String modifiedBy) {
        if (createdDate != null) {
            setCreatedDate(createdDate.getTime());
        }

        if (modifiedDate != null) {
            setModifiedDate(modifiedDate.getTime());
        }
        setCreatedBy(createdBy);

        setModifiedBy(modifiedBy);
    }
}
