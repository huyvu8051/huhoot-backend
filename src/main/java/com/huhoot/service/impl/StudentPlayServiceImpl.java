package com.huhoot.service.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.dto.StudentAnswerRequest;
import com.huhoot.model.*;
import com.huhoot.repository.AnswerRepository;
import com.huhoot.repository.QuestionRepository;
import com.huhoot.repository.StudentAnswerRepository;
import com.huhoot.repository.StudentInChallengeRepository;
import com.huhoot.service.StudentPlayService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentPlayServiceImpl implements StudentPlayService {

    private final SocketIOServer socketIOServer;

    private final StudentInChallengeRepository studentInChallengeRepository;

    public StudentPlayServiceImpl(SocketIOServer socketIOServer, StudentInChallengeRepository studentInChallengeRepository, AnswerRepository answerRepository, StudentAnswerRepository studentAnswerRepository, QuestionRepository questionRepository) {
        this.socketIOServer = socketIOServer;
        this.studentInChallengeRepository = studentInChallengeRepository;
        this.answerRepository = answerRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void join(int challengeId, Student userDetails) throws NotFoundException {

        Optional<StudentInChallenge> optional = studentInChallengeRepository.findOneByPrimaryKeyChallengeIdAndPrimaryKeyStudentId(challengeId, userDetails.getId());

        StudentInChallenge studentInChallenge = optional.get();

        studentInChallenge.setLogin(true);

        studentInChallengeRepository.save(studentInChallenge);


    }

    private final AnswerRepository answerRepository;

    private final StudentAnswerRepository studentAnswerRepository;

    private final QuestionRepository questionRepository;

    @Override
    public Integer answer(StudentAnswerRequest request, Student userDetails) throws NotFoundException {

        Timestamp now = new Timestamp(System.currentTimeMillis());

        List<Answer> correctAnswers = answerRepository.findAllByIdInAndIsCorrectTrue(request.getAnswerIds());



        boolean allAnswerInCorrect = true;

        for (Integer answerId : request.getAnswerIds()) {
            if(!correctAnswers.stream().anyMatch(e->e.getId()==answerId)){
                allAnswerInCorrect = false;
                break;
            }
        }

        boolean correctAnswer = allAnswerInCorrect && (request.getAnswerIds().size() == correctAnswers.size());




        // test optimize
        Challenge challenge = new Challenge();
        challenge.setId(request.getChallengeId());

        Optional<Question> optional = questionRepository.findOneById(request.getQuestionId());

        Question quest = optional.orElseThrow(()->new NotFoundException("Question not found"));



        for(Answer ans : correctAnswers){
            StudentAnswer studentAnswer = StudentAnswer.builder()
                    .primaryKey(StudentAnswerId.builder()
                            .answer(ans)
                            .challenge(challenge)
                            .question(quest)
                            .student(userDetails)
                            .build())
                    .score(69)
                    .isCorrect(correctAnswer)
                    .answerDate(now)
                    .build();

            try{
                studentAnswerRepository.save(studentAnswer);
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }



        return null;
    }
}
