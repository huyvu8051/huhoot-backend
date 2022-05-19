package com.huhoot.host.manage.question;

import com.huhoot.converter.ListConverter;
import com.huhoot.converter.QuestionConverter;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.encrypt.EncryptUtils;
import com.huhoot.mapper.QuestionMapper;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.model.Question;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.QuestionRepository;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ManageQuestionServiceImpl implements ManageQuestionService {

    private final ChallengeRepository challengeRepository;
    private final QuestionMapper questionMapper;
    private final ListConverter listConverter;
    private final QuestionRepository questionRepository;

    private final EncryptUtils encryptUtils;

    @Override
    public PageResponse<QuestionResponse> findAllQuestionInChallenge(Admin userDetails, int challengeId, Pageable pageable) {
        Page<QuestionResponse> questions = questionRepository.findAllByChallengeIdAndAdminId(challengeId, userDetails.getId(), pageable);
        return listConverter.toPageResponse(questions);
    }

    @Override
    public QuestionResponse addOneQuestion(Admin userDetails, QuestionAddRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NullPointerException {
        Optional<Challenge> optional = challengeRepository.findOneById(request.getChallengeId());

        Challenge challenge = optional.orElseThrow(() -> new NullPointerException("Challenge not found"));

        checker.accept(userDetails, challenge);

        Question question = QuestionConverter.toEntity(request);

        int nextOrdinalNumber = questionRepository.getNextOrdinalNumber(challenge.getId());

        question.setOrdinalNumber(nextOrdinalNumber);
        question.setChallenge(challenge);

        byte[] byteKey = encryptUtils.generateRandomKey().getEncoded();
        question.setEncryptKey(byteKey);

        Question save = questionRepository.save(question);

        return questionMapper.toDto(save);

    }

    @Override
    public void updateOneQuestion(Admin userDetails, QuestionUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NullPointerException {
        Optional<Question> optional = questionRepository.findOneById(request.getId());

        Question question = optional.orElseThrow(() -> new NullPointerException("Question not found"));

        checker.accept(userDetails, question.getChallenge());

        questionMapper.update(request, question);

        questionRepository.save(question);
    }

    @Override
    public void updateOrdinal(Admin userDetails, QuestionOrdinalUpdateRequest request) {
        List<QuestionOrdinal> list = request.getList();

        List<Question> result = new ArrayList<>();
        for (QuestionOrdinal dto : list) {
            Optional<Question> optional = questionRepository.findOneById(dto.getId());
            Question entity = optional.orElseThrow(() -> new NullPointerException("Question not found"));
            entity.setOrdinalNumber(dto.getOrdinalNumber());
            result.add(entity);
        }
        questionRepository.saveAll(result);
    }
}
