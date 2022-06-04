package com.huhoot.dto;

import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetStudentDetailsRes {
    private StudentResponse studentDetails;
    private PageResponse<ChallengeResponse> listChallenge;
}
