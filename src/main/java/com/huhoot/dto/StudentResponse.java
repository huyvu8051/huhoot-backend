package com.huhoot.dto;

import com.huhoot.auditing.AuditableDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class StudentResponse extends AuditableDto {
    private int id;
    private String username;
    private String fullName;


    private Boolean isNonLocked;

    public StudentResponse(int id, String username, String fullName, Long createdDate, String createdBy, Long modifiedDate, String modifiedBy, Boolean isNonLocked) {
        super(createdDate, createdBy, modifiedDate, modifiedBy);
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.isNonLocked = isNonLocked;
    }
}
