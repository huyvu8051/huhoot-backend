package com.huhoot.host.manage.question;

import com.huhoot.converter.ListConverter;
import com.huhoot.converter.QuestionConverter;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.mapper.QuestionMapper;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.model.Question;
import com.huhoot.repository.ChallengeRepository;
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
public class ManageQuestionServiceImpl implements ManageQuestionService{

    private final ChallengeRepository challengeRepository;
    private final QuestionMapper questionMapper;
    private final ListConverter listConverter;
    private final QuestionRepository questionRepository;
    @Override
    public PageResponse<QuestionResponse> findAllQuestionInChallenge(Admin userDetails, int challengeId, Pageable pageable) {
        Page<QuestionResponse> questions = questionRepository.findAllByChallengeIdAndAdminId(challengeId, userDetails.getId(), pageable);
        return listConverter.toPageResponse(questions);
    }
    @Override
    public QuestionResponse addOneQuestion(Admin userDetails, QuestionAddRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Challenge> optional = challengeRepository.findOneById(request.getChallengeId());

        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        checker.accept(userDetails, challenge);

        Question question = QuestionConverter.toEntity(request);

        question.setChallenge(challenge);

        Question save = questionRepository.save(question);

        return questionMapper.toDto(save);

    }
    @Override
    public void updateOneQuestion(Admin userDetails, QuestionUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Question> optional = questionRepository.findOneById(request.getId());

        Question question = optional.orElseThrow(() -> new NotFoundException("Question not found"));

        checker.accept(userDetails, question.getChallenge());

        questionMapper.update(request, question);

        questionRepository.save(question);

    }
}
