package com.huhoot.dto;

import com.huhoot.organize.StudentScoreResponse;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetTopStudentRes {
    private PageResponse<StudentScoreResponse> topStudents;
    private ChallengeResponse challengeResponse;

}
