package com.huhoot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
public class StudentInChallengeResponse {
    private int studentId;
    private String studentUsername;
    private String studentFullName;
    private Boolean isLogin;
    private Boolean isKicked;
    private Boolean isOnline;

    private String createdBy;
    private Long createdDate;
    private String modifiedBy;
    private Long modifiedDate;

    private Boolean isNonDeleted;

    public StudentInChallengeResponse(int studentId, String studentUsername, String studentFullName, Boolean isLogin, Boolean isKicked, Boolean isOnline, String createdBy, Date createdDate, String modifiedBy, Date modifiedDate, Boolean isNonDeleted) {
        this.studentId = studentId;
        this.studentUsername = studentUsername;
        this.studentFullName = studentFullName;
        this.isLogin = isLogin;
        this.isKicked = isKicked;
        this.isOnline = isOnline;
        this.createdBy = createdBy;
        if(createdDate != null){
            this.createdDate = createdDate.getTime();
        }
        this.modifiedBy = modifiedBy;

        if(modifiedDate != null){
            this.modifiedDate = modifiedDate.getTime();
        }

        this.isNonDeleted = isNonDeleted;
    }
}
