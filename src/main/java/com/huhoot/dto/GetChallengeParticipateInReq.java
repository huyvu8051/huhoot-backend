package com.huhoot.dto;

import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.Data;

@Data
public class GetChallengeParticipateInReq extends VDataTablePagingRequest {
    private String username;
}
