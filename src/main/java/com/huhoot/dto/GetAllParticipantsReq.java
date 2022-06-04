package com.huhoot.dto;

import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.Data;

@Data
public class GetAllParticipantsReq  extends VDataTablePagingRequest {
    private int challengeId;
}
