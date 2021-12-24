package com.huhoot.host.manage.question;

import com.huhoot.model.Admin;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ManageQuestionService {

    PageResponse<QuestionResponse> findAllQuestionInChallenge(Admin userDetails, int challengeId, Pageable pageable);
}
