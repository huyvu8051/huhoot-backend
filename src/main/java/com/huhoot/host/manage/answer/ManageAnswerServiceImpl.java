package com.huhoot.host.manage.answer;

import com.huhoot.converter.AnswerConverter;
import com.huhoot.converter.ListConverter;
import com.huhoot.host.organize.PublishAnswer;
import com.huhoot.mapper.AnswerMapper;
import com.huhoot.model.Admin;
import com.huhoot.model.Answer;
import com.huhoot.model.Question;
import com.huhoot.repository.AnswerRepository;
import com.huhoot.repository.QuestionRepository;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ManageAnswerServiceImpl implements ManageAnswerService {
    private final AnswerRepository answerRepository;
    private final ListConverter listConverter;
    private final QuestionRepository questionRepository;
    @Override
    public PageResponse<PublishAnswer> findAllAnswerByQuestionId(Admin userDetails, int questionId, Pageable pageable) {
        Page<PublishAnswer> answers = answerRepository.findAllByQuestionIdAndAdminId(questionId, userDetails.getId(), pageable);

        return listConverter.toPageResponse(answers);
    }


    @Override
    public void addOneAnswer(Admin userDetails, AnswerAddRequest request) throws Exception {
        Optional<Question> optional = questionRepository.findOneById(request.getQuestionId());

        Question question = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        Answer answer = AnswerConverter.toEntity(request);

        answer.setQuestion(question);

        answerRepository.save(answer);

    }

    private final AnswerMapper answerMapper;

    @Override
    public void updateOneAnswer(Admin userDetails, AnswerUpdateRequest request) throws NotFoundException {
        Optional<Answer> optional = answerRepository.findOneById(request.getId());

        Answer answer = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        answerMapper.updateAnswer(request, answer);

        answerRepository.save(answer);
    }
}
