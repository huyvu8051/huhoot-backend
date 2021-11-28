package com.huhoot.service.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.dto.StudentAnswerRequest;
import com.huhoot.enums.Points;
import com.huhoot.model.*;
import com.huhoot.repository.*;
import com.huhoot.service.StudentPlayService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StudentPlayServiceImpl implements StudentPlayService {

    private final SocketIOServer socketIOServer;

    private final StudentInChallengeRepository studentInChallengeRepository;

    public StudentPlayServiceImpl(SocketIOServer socketIOServer, StudentInChallengeRepository studentInChallengeRepository, AnswerRepository answerRepository, StudentAnswerRepository studentAnswerRepository, QuestionRepository questionRepository, ChallengeRepository challengeRepository) {
        this.socketIOServer = socketIOServer;
        this.studentInChallengeRepository = studentInChallengeRepository;
        this.answerRepository = answerRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionRepository = questionRepository;
        this.challengeRepository = challengeRepository;
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

    private final ChallengeRepository challengeRepository;

    @Override
    public int answer(StudentAnswerRequest request, Student userDetails) throws NotFoundException {

        // check is answered by query is any student answer have answer date not null
        // not check yet


        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<Integer> correctAnswers = answerRepository.findAllCorrectAnswerIds(request.getQuestionId());

        boolean isAnswersCorrect = isAnswerCorrect(request.getAnswerIds(), correctAnswers);


        Optional<Challenge> optionalChallenge = challengeRepository.findOneById(request.getChallengeId());
        Challenge challenge = optionalChallenge.orElseThrow(() -> new NotFoundException("Challenge not found"));


        // find question with askDate not null help prevent hacker try to get correct answer
        // they only can get question if it has been published
        Optional<Question> optional = questionRepository.findOneByIdAndAskDateNotNull(request.getQuestionId());
        Question quest = optional.orElseThrow(() -> new NotFoundException("Question not found"));

        UUID adminSocketId = challenge.getAdmin().getSocketId();


        List<Answer> answers = answerRepository.findAllByIdIn(request.getAnswerIds());

        double point = isAnswersCorrect ? calculatePoint(quest.getAskDate(), now, quest.getPoint(), quest.getAnswerTimeLimit()) : 0;

        for (Answer ans : answers) {
            try {
                studentAnswerRepository.updateAnswer(point, isAnswersCorrect, now, ans.getId(), userDetails.getId());

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        // sent socket to host notice answered

        socketIOServer.getClient(adminSocketId).sendEvent("studentAnswer", quest.getId());

        int totalScore = studentAnswerRepository.getTotalPointInChallenge(request.getChallengeId(), userDetails.getId());

        return totalScore;
    }

    private double calculatePoint(Timestamp askDate, Timestamp now, Points point, int answerTimeLimit) {

        long diff = now.getTime() - askDate.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long timeLeft = answerTimeLimit - seconds;

        if (timeLeft < 0) {
            return 0;
        }

        double timeLeftPercent = timeLeft * 1.0 / answerTimeLimit;

        return 500 + (500 * timeLeftPercent) * point.getValue();
    }


    private boolean isAnswerCorrect(List<Integer> answerIds, List<Integer> correctAnswerIds) {
        final boolean a = answerIds.stream().allMatch(e -> correctAnswerIds.contains(e));
        // final boolean b = correctAnswerIds.stream().allMatch(e -> answerIds.contains(e));
        return  a && correctAnswerIds.stream().allMatch(e -> answerIds.contains(e));

    }
}
