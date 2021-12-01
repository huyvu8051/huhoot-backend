package com.huhoot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentAddErrorResponse extends StudentAddRequest {
    private String errorMessage;

    public StudentAddErrorResponse(StudentAddRequest studentAddRequest) {

    }
}
