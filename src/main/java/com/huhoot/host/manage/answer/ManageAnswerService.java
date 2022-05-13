package com.huhoot.host.manage.answer;

import com.huhoot.organize.PublishAnswer;
import com.huhoot.model.Admin;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ManageAnswerService {

    PageResponse<PublishAnswer> findAllAnswerByQuestionId(Admin userDetails, int questionId, Pageable pageable);

    void addOneAnswer(Admin userDetails, AnswerAddRequest request) throws Exception;

    void updateOneAnswer(Admin userDetails, AnswerUpdateRequest request);

    void updateOrdinal(Admin userDetails, AnswerOrdinalUpdateRequest request);
}
