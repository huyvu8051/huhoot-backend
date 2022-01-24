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
import com.huhoot.socket.SocketRegisterSuccessResponse;
import com.huhoot.student.participate.StudentParticipateService;
import com.huhoot.student.participate.SendAnswerResponse;
import com.huhoot.student.participate.StudentAnswerRequest;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.Serializable;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        if (student.getSocketId() != null && !student.getSocketId().equals(client.getSessionId())) {
            try {
                SocketIOClient oldClient = socketIOServer.getClient(student.getSocketId());
                oldClient.sendEvent("joinError", "joinError");
                oldClient.disconnect();
            } catch (Exception e) {
                log.info("Cannot disconnect old client or client not found!");
            }
        }


        double totalPoints = studentAnswerRepository.getTotalPointInChallenge(challengeId, student.getId());

        client.joinRoom(challengeId + "");
        client.sendEvent("registerSuccess", SocketRegisterSuccessResponse.builder()
                .totalPoints(totalPoints)
                .build());
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
        long nowLong = System.currentTimeMillis();


        // find question with askDate not null help prevent hacker try to get correct answer
        // they only can get question if it has been published
        Question quest = questionRepository.findOneByIdAndAskDateNotNull(request.getQuestionId()).orElseThrow(() -> new NotFoundException("Question not found"));


        // isAnswersCorrect
        String decrypt = EncryptUtil.decrypt(request.getHashCorrectAnswerIds(), quest.getEncryptKey());
        String collect = request.getAnswerIds().stream().sorted().map(e -> e.toString()).collect(Collectors.joining(""));
        boolean isAnswersCorrect = decrypt.equals(collect);

        double point = isAnswersCorrect ? calculatePoint(quest.getAskDate(), nowLong, quest.getPoint().getValue(), quest.getAnswerTimeLimit()) : 0;


        studentAnswerRepository.updateAnswerPoint(request.getAnswerIds(), userDetails.getId(), point / request.getAnswerIds().size(), isAnswersCorrect, nowLong);


        // sent socket to host notice answered

        UUID adminSocketId = UUID.fromString(request.getAdminSocketId());
        socketIOServer.getClient(adminSocketId).sendEvent("studentAnswer", quest.getId());

        // response encrypt message
        byte[] byteKey = quest.getEncryptKey();
        String pointsReceived = EncryptUtil.encrypt(point + "", byteKey);

        return SendAnswerResponse.builder()
                .pointsReceived(pointsReceived).build();
    }


    private double calculatePoint(long askDate, long now, int pointCoefficient, int answerTimeLimit) {

        long timeLeft = askDate + (answerTimeLimit * 1000) - now;

        if (timeLeft <= 0) {
            return 0;
        }

        double timeLeftPercent = timeLeft * 1.0 / (answerTimeLimit * 1000);

        int defaultCorrectPoint = 500;

        return (defaultCorrectPoint + (500 * timeLeftPercent) * pointCoefficient);
    }


}
