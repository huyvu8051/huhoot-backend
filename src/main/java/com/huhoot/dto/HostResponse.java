package com.huhoot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HostResponse {
    private int id;
    private String username;
    private boolean isNonLocked;
    private Date createdDate;
    private Date modifiedDate;

}
