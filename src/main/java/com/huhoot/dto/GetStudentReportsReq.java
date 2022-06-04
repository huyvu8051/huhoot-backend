package com.huhoot.dto;

import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.Data;

@Data
public class GetStudentReportsReq extends VDataTablePagingRequest {
    private int challengeId;
    private String username;
}
