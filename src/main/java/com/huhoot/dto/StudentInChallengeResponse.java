package com.huhoot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInChallengeResponse {
    private int studentId;
    private String studentUsername;
    private String studentFullName;
    private Boolean isLogin;
    private Boolean isKicked;
    private Boolean isOnline;

    private String createdBy;
    private Timestamp createdDate;
    private String modifiedBy;
    private Timestamp modifiedDate;

    private Boolean isNonDeleted;

}
