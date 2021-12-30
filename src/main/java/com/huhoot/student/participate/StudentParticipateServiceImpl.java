package com.huhoot.student.participate;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.repository.StudentRepository;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.enums.Points;
import com.huhoot.exception.ChallengeException;
import com.huhoot.host.organize.EncryptUtil;
import com.huhoot.model.*;
import com.huhoot.repository.*;
import com.huhoot.student.participate.StudentParticipateService;
import com.huhoot.student.participate.SendAnswerResponse;
import com.huhoot.student.participate.StudentAnswerRequest;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class StudentParticipateServiceImpl implements StudentParticipateService {

    private final SocketIOServer socketIOServer;

    private final StudentInChallengeRepository studentInChallengeRepository;

    private final StudentRepository studentRepository;

    @Override
    public void join(SocketIOClient client, int challengeId, Student student) throws ChallengeException {

        Optional<StudentInChallenge> optional = studentInChallengeRepository.findOneByChallengeIdAndStudentIdAndAvailable(challengeId, student.getId());
        StudentInChallenge studentInChallenge = optional.orElseThrow(() -> new ChallengeException("Challenge not available!"));
        Challenge challenge = studentInChallenge.getChallenge();
        ChallengeStatus challengeStatus = challenge.getChallengeStatus();

        if (challengeStatus.equals(ChallengeStatus.IN_PROGRESS) || challengeStatus.equals(ChallengeStatus.LOCKED)) {
            if (!studentInChallenge.isLogin()) {
                throw new ChallengeException("Challenge not available!");
            }
        }
        // if another device connect to server, disconnect old client
        // Prevent multi device connect to server
        if(student.getSocketId() != null && !student.getSocketId().equals(client.getSessionId())){
            try {
                SocketIOClient oldClient = socketIOServer.getClient(student.getSocketId());
                oldClient.sendEvent("joinError", "joinError");
                oldClient.disconnect();
            } catch (Exception e) {
                log.info("Cannot disconnect old client or client not found!");
            }
        }

        client.joinRoom(challengeId + "");
        // update socket id
        studentRepository.updateSocketId(client.getSessionId(), student.getId());
        studentInChallenge.setLogin(true);
        studentInChallengeRepository.save(studentInChallenge);

    }

    private final AnswerRepository answerRepository;

    private final StudentAnswerRepository studentAnswerRepository;

    private final QuestionRepository questionRepository;

    private final ChallengeRepository challengeRepository;

    @Override
    public SendAnswerResponse answer(StudentAnswerRequest request, Student userDetails) throws Exception {
        // check is answered by query is any student answer have answer date not null
        // not check yet

        // check after skip question, student can send answer ... not checked

        // the question answer option not match. if single select but multi answer @@

        // multi correct answer, point may / for num of correct answer

        // open challenge must validate num of correct answer > 0
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<Integer> correctAnswers = answerRepository.findAllCorrectAnswerIds(request.getQuestionId());
        boolean isAnswersCorrect = isAnswerCorrect(request.getAnswerIds(), correctAnswers);
        // find question with askDate not null help prevent hacker try to get correct answer
        // they only can get question if it has been published
        Optional<Question> optional = questionRepository.findOneByIdAndAskDateNotNull(request.getQuestionId());
        Question quest = optional.orElseThrow(() -> new NotFoundException("Question not found"));
        List<Answer> answers = answerRepository.findAllByIdIn(request.getAnswerIds());
        double point = isAnswersCorrect ? calculatePoint(quest.getAskDate(), now, quest.getPoint(), quest.getAnswerTimeLimit(), correctAnswers.size()) : 0;
        for (Answer ans : answers) {
            try {
                studentAnswerRepository.updateAnswer(point, isAnswersCorrect, now, ans.getId(), userDetails.getId());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        // sent socket to host notice answered
        Optional<Challenge> optionalChallenge = challengeRepository.findOneById(request.getChallengeId());
        Challenge challenge = optionalChallenge.orElseThrow(() -> new NotFoundException("Challenge not found"));
        UUID adminSocketId = challenge.getAdmin().getSocketId();
        socketIOServer.getClient(adminSocketId).sendEvent("studentAnswer", quest.getId());

        int totalPoint = studentAnswerRepository.getTotalPointInChallenge(request.getChallengeId(), userDetails.getId());
        // response encrypt message
        byte[] byteKey = quest.getEncryptKey();
        String isAnswerCorrectEncrypted = EncryptUtil.encrypt(isAnswersCorrect + "", byteKey);
        String totalPointEncrypted = EncryptUtil.encrypt(totalPoint + "", byteKey);
        return SendAnswerResponse.builder()
                .isCorrect(isAnswerCorrectEncrypted)
                .totalPoint(totalPointEncrypted)
                .build();
    }

    private double calculatePoint(Timestamp askDate, Timestamp now, Points point, int answerTimeLimit, int numOfCorrectAnswer) {

        long diff = now.getTime() - askDate.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long timeLeft = answerTimeLimit - seconds;

        if (timeLeft < 0) {
            return 0;
        }

        double timeLeftPercent = timeLeft * 1.0 / answerTimeLimit;

        return (500 + (500 * timeLeftPercent) * point.getValue()) / numOfCorrectAnswer;
    }


    private boolean isAnswerCorrect(List<Integer> answerIds, List<Integer> correctAnswerIds) {
        final boolean a = correctAnswerIds.containsAll(answerIds);
        // final boolean b = correctAnswerIds.stream().allMatch(e -> answerIds.contains(e));
        return a && answerIds.containsAll(correctAnswerIds);

    }
}
