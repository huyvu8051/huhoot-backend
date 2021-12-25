package com.huhoot.host.manage.answer;

import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.Data;

@Data
public class FindAllAnswerRequest extends VDataTablePagingRequest {
    private int questionId;


}
