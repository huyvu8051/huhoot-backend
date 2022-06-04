package com.huhoot.dto;

import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.Data;

@Data
public class GetStudentDetailsReq extends VDataTablePagingRequest {
    private String username;
}
