package com.huhoot.admin.host;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostResponse {
    private int id;
    private String username;
    private Boolean isNonLocked;
    private Long createdDate;
    private String createdBy;
    private Long modifiedDate;
    private String modifiedBy;

    public HostResponse(int id, String username, Boolean isNonLocked, Date createdDate, String createdBy, Date modifiedDate, String modifiedBy) {
        this.id = id;
        this.username = username;
        this.isNonLocked = isNonLocked;
        if (createdDate != null) {
            this.createdDate = createdDate.getTime();
        }
        this.createdBy = createdBy;
        if (modifiedDate != null) {
            this.modifiedDate = modifiedDate.getTime();
        }
        this.modifiedBy = modifiedBy;
    }
}
