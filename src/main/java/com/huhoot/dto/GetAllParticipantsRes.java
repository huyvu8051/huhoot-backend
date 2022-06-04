package com.huhoot.dto;

import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAllParticipantsRes {
    private PageResponse<StudentInChallengeResponse> participants;
    private ChallengeResponse challengeResponse;
}
