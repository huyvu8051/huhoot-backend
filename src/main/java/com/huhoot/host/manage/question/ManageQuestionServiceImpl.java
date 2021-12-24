package com.huhoot.host.manage.question;

import com.huhoot.converter.ListConverter;
import com.huhoot.converter.QuestionConverter;
import com.huhoot.model.Admin;
import com.huhoot.model.Question;
import com.huhoot.repository.QuestionRepository;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ManageQuestionServiceImpl implements ManageQuestionService{

    private final ListConverter listConverter;
    private final QuestionRepository questionRepository;
    @Override
    public PageResponse<QuestionResponse> findAllQuestionInChallenge(Admin userDetails, int challengeId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByChallengeIdAndChallengeAdminId(challengeId, userDetails.getId(), pageable);
        return listConverter.toPageResponse(questions, QuestionConverter::toQuestionResponse);
    }
}
