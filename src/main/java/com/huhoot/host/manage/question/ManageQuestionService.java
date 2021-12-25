package com.huhoot.host.manage.question;

import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

public interface ManageQuestionService {

    PageResponse<QuestionResponse> findAllQuestionInChallenge(Admin userDetails, int challengeId, Pageable pageable);

    QuestionResponse addOneQuestion(Admin userDetails, QuestionAddRequest request, CheckedFunction<Admin, Challenge> checker) throws NotFoundException, NotYourOwnException;

    void updateOneQuestion(Admin userDetails, QuestionUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException;


}
