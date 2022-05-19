package com.huhoot.participate;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.auth.JwtUtil;
import com.huhoot.encrypt.EncryptUtils;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.exception.ChallengeException;
import com.huhoot.model.Challenge;
import com.huhoot.model.Question;
import com.huhoot.model.Student;
import com.huhoot.model.StudentInChallenge;
import com.huhoot.repository.QuestionRepository;
import com.huhoot.repository.StudentAnswerRepository;
import com.huhoot.repository.StudentInChallengeRepository;
import com.huhoot.repository.StudentRepository;
import com.huhoot.socket.SocketRegisterSuccessResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class ParticipateServiceImpl implements ParticipateService {

    private final SocketIOServer socketIOServer;

    private final StudentInChallengeRepository studentInChallengeRepository;

    private final StudentRepository studentRepository;


    private final StudentAnswerRepository studentAnswerRepository;

    private final QuestionRepository questionRepository;

    private final JwtUtil jwtUtil;
    private final EncryptUtils encryptUtils;

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



    @Override
    public SendAnswerResponse sendAnswer(StudentAnswerRequest request, Student student) throws Exception {


        long nowLong = System.currentTimeMillis();


        Long answerTimeLimit = encryptUtils.decryptQuestionToken(request.getQuestionToken(), request.getAnswerIds());


        Question quest = questionRepository.findOneByIdAndAskDateNotNull(request.getQuestionId()).orElseThrow(() -> new NullPointerException("question not found"));

        int comboCount = 0;
        double pointReceive = 0;

        boolean isAnswersCorrect;

        if (answerTimeLimit == null) {
            // wrong answer
            isAnswersCorrect = false;

        } else if (answerTimeLimit >= nowLong) {
            // correct answer
            isAnswersCorrect = true;
            comboCount = encryptUtils.extractCombo(request.getComboToken(), student.getUsername(), quest.getPublishedOrderNumber());
            comboCount++;
            pointReceive = calculatePoint(quest.getAskDate(), nowLong, quest.getPoint().getValue(), quest.getAnswerTimeLimit(), comboCount);

        } else {
            // out of time limit


            return null;
        }


        studentAnswerRepository.updateAnswerPoint(request.getAnswerIds(), student.getId(), pointReceive / request.getAnswerIds().size(), isAnswersCorrect, nowLong);


        // sent socket to host notice answered
        UUID adminSocketId = UUID.fromString(request.getAdminSocketId());
        socketIOServer.getClient(adminSocketId).sendEvent("studentAnswer");

        // encrypt response message
        String nextComboToken = encryptUtils.prepareComboToken(student.getUsername(), quest.getPublishedOrderNumber() + 1, comboCount);

        String encryptedResponse = encryptUtils.genEncryptedResponse(pointReceive, comboCount, quest.getEncryptKey());

        return SendAnswerResponse.builder()
                .comboToken(nextComboToken)
                .encryptedResponse(encryptedResponse)
                .build();

    }


    private double calculatePoint(long askDate, long now, int pointCoefficient, int answerTimeLimit, int combo) {

        long timeLeft = askDate + (answerTimeLimit * 1000L) - now;

        if (timeLeft <= 0) {
            return 0;
        }

        double timeLeftPercent = timeLeft * 1.0 / (answerTimeLimit * 1000);

        int defaultCorrectPoint = 500;

        return (defaultCorrectPoint + (500 * timeLeftPercent)) * pointCoefficient + combo * 50;
    }


}
