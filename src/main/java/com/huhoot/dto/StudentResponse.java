package com.huhoot.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class StudentResponse {
    private int id;
    private String username;
    private String fullName;

    private Date createdDate;
    private String createdBy;
    private Timestamp modifiedDate;
    private String modifiedBy;

    private Boolean isNonLocked;
}
