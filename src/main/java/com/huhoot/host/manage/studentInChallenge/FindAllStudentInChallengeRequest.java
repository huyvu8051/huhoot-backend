package com.huhoot.host.manage.studentInChallenge;

import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.Data;

@Data
public class FindAllStudentInChallengeRequest extends VDataTablePagingRequest {
    private int challengeId;


}
