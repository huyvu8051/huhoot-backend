package com.huhoot.host.manage.question;

import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.Data;

@Data
public class FindAllQuestionRequest extends VDataTablePagingRequest {
    private int challengeId;


}
