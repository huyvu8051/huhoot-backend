package com.huhoot.admin.manage.student;

import lombok.Data;

@Data
public class StudentUpdateRequest {
    private int id;
    private String fullName;
    private String username;
    private Boolean isNonLocked;
}
