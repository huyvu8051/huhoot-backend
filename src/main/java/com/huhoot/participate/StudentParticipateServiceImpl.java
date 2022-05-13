package com.huhoot.participate;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.auth.JwtUtil;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.exception.ChallengeException;
import com.huhoot.organize.EncryptUtil;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class StudentParticipateServiceImpl implements StudentParticipateService {

    private final SocketIOServer socketIOServer;

    private final StudentInChallengeRepository studentInChallengeRepository;

    private final StudentRepository studentRepository;


    private final StudentAnswerRepository studentAnswerRepository;

    private final QuestionRepository questionRepository;
    private final JwtUtil jwtUtil;

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

        Question quest = questionRepository.findOneByIdAndAskDateNotNull(request.getQuestionId()).orElseThrow(() -> new NullPointerException("Question not found"));

        // sinh viên gửi lên 1 cái danh sách id đáp án(bị sắp xếp + nối chuỗi)

        // isAnswersCorrect
        String decrypt = EncryptUtil.decrypt(request.getHashCorrectAnswerIds(), quest.getEncryptKey());
        String collect = request.getAnswerIds().stream().sorted().map(e -> e.toString()).collect(Collectors.joining(""));
        boolean isAnswersCorrect = decrypt.equals(collect);

        int comboCount = jwtUtil.extractCombo(request.getComboToken(), student.getUsername(), quest.getPublishedOrderNumber() + "");

        double point = isAnswersCorrect ? calculatePoint(quest.getAskDate(), nowLong, quest.getPoint().getValue(), quest.getAnswerTimeLimit()) : 0;
        studentAnswerRepository.updateAnswerPoint(request.getAnswerIds(), student.getId(), point / request.getAnswerIds().size(), isAnswersCorrect, nowLong);


        // sent socket to host notice answered
        UUID adminSocketId = UUID.fromString(request.getAdminSocketId());
        socketIOServer.getClient(adminSocketId).sendEvent("studentAnswer", quest.getId());

        // encrypt response message
        byte[] byteKey = quest.getEncryptKey();
        String pointsReceived = EncryptUtil.encrypt(point + "", byteKey);


        if(isAnswersCorrect){
            comboCount ++;
        }else {
            comboCount = 0;
        }

        String nextQuestionSign = quest.getPublishedOrderNumber() + 1 + "";

        String comboToken = jwtUtil.createComboToken(student.getUsername(), nextQuestionSign, comboCount + "");

        String comboEncrypted = EncryptUtil.encrypt(comboCount + "", byteKey);

        jwtUtil.testToken(student);
        return SendAnswerResponse.builder()
                .comboToken(comboToken)
                .combo(comboEncrypted)
                .pointsReceived(pointsReceived)
                .build();

    }


    private double calculatePoint(long askDate, long now, int pointCoefficient, int answerTimeLimit) {

        long timeLeft = askDate + (answerTimeLimit * 1000L) - now;

        if (timeLeft <= 0) {
            return 0;
        }

        double timeLeftPercent = timeLeft * 1.0 / (answerTimeLimit * 1000);

        int defaultCorrectPoint = 500;

        return (defaultCorrectPoint + (500 * timeLeftPercent)) * pointCoefficient;
    }


}
