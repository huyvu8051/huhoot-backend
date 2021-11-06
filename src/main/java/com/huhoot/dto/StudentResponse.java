package com.huhoot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StudentResponse {
    private int id;
    private String username;
    private String fullName;
    private Date createdDate;
    private Date modifiedDate;
    private boolean isNonLocked;
}
