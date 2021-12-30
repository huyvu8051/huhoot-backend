package com.huhoot.admin.manage.host;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostUpdateRequest {
    private int id;
    private String username;
    private Boolean isNonLocked;
}
