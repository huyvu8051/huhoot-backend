package com.huhoot.admin.manage.host;

import com.huhoot.auditing.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostResponse extends AuditableDto {
    private int id;
    private String username;
    private Boolean isNonLocked;

    public HostResponse(int id, String username, Boolean isNonLocked, Date createdDate, String createdBy, Date modifiedDate, String modifiedBy) {
        super(createdDate, createdBy, modifiedDate, modifiedBy);
        this.id = id;
        this.username = username;
        this.isNonLocked = isNonLocked;

    }
}
