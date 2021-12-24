package com.huhoot.admin.manage.host;

import lombok.Data;

@Data
public class HostUpdateRequest {
    private int id;
    private String username;
    private Boolean isNonLocked;
}
