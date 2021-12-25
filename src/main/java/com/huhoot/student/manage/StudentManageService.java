package com.huhoot.student.manage;

import com.huhoot.host.manage.challenge.ChallengeResponse;
import com.huhoot.model.Student;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import org.springframework.data.domain.Pageable;

public interface StudentManageService {
    PageResponse<ChallengeResponse> findAllChallenge(Student userDetails, Pageable pageable);
}
