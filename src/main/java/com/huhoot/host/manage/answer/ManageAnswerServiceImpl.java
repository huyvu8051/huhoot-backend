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

import java.util.ArrayList;
import java.util.List;
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
        int nextOrdinalNumber = answerRepository.getNextOrdinalNumber(question.getId());
        answer.setQuestion(question);
        answer.setOrdinalNumber(nextOrdinalNumber);
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

    @Override
    public void updateOrdinal(Admin userDetails, AnswerOrdinalUpdateRequest request) throws NotFoundException {
        List<AnswerOrdinal> list = request.getList();
        List<Answer> result = new ArrayList<>();

        for (AnswerOrdinal dto : list) {
            Optional<Answer> optional = answerRepository.findOneById(dto.getId());
            Answer entity = optional.orElseThrow(() -> new NotFoundException("Question not found"));
            entity.setOrdinalNumber(dto.getOrdinalNumber());
         result.add(entity);
        }

        answerRepository.saveAll(result);
    }
}
