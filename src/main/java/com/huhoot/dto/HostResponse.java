package com.huhoot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HostResponse {
    private int id;
    private String username;
    private Boolean isNonLocked;

    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;

}
