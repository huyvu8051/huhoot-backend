package com.huhoot.participate;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.auth.JwtUtil;
import com.huhoot.encrypt.EncryptUtils;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.exception.ChallengeException;
import com.huhoot.host.manage.challenge.ChallengeMapper;
import com.huhoot.host.manage.challenge.ChallengeResponse;
import com.huhoot.model.Challenge;
import com.huhoot.model.Question;
import com.huhoot.model.Student;
import com.huhoot.model.StudentInChallenge;
import com.huhoot.organize.AnswerResultResponse;
import com.huhoot.organize.PublishAnswer;
import com.huhoot.organize.PublishQuestion;
import com.huhoot.organize.PublishedExam;
import com.huhoot.repository.*;
import com.huhoot.socket.ParticipateJoinSuccessRes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    private final AnswerRepository answerRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;


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
        client.sendEvent("registerSuccess", ParticipateJoinSuccessRes.builder().totalPoints(totalPoints).currentExam(this.getCurrentPublishedExam(challengeId)).build());
        // update socket id
        studentRepository.updateSocketId(client.getSessionId(), student.getId());
        studentInChallenge.setLogin(true);
        studentInChallengeRepository.save(studentInChallenge);

    }

    public PublishedExam getCurrentPublishedExam(int challengeId) {
        Optional<Question> op = questionRepository.findFirstByChallengeIdAndAskDateNotNullOrderByAskDateDesc(challengeId);

        Challenge challenge = challengeRepository.findOneById(challengeId).orElseThrow(() -> new NullPointerException("Challenge not found"));
        ChallengeResponse challengeResponse = challengeMapper.toDto(challenge);
        if (!op.isPresent()) {


            return PublishedExam.builder().challenge(challengeResponse).build();
        }

        Question currQuestion = op.get();

        int countQuestion = questionRepository.countQuestionInChallenge(challengeId);
        int questionOrder = questionRepository.findNumberOfPublishedQuestion(challengeId) + 1;

        PublishQuestion publishQuest = PublishQuestion.builder().id(currQuestion.getId()).ordinalNumber(currQuestion.getOrdinalNumber()).askDate(currQuestion.getAskDate()).questionContent(currQuestion.getQuestionContent()).questionImage(currQuestion.getQuestionImage()).answerTimeLimit(currQuestion.getAnswerTimeLimit()).point(currQuestion.getPoint()).answerOption(currQuestion.getAnswerOption()).challengeId(challengeId).totalQuestion(countQuestion).questionOrder(questionOrder).theLastQuestion(countQuestion == questionOrder).build();

        List<PublishAnswer> publishAnswers2 = answerRepository.findAllAnswerByQuestionIdAndAdminId(currQuestion.getId());

        String questionToken = encryptUtils.generateQuestionToken(publishAnswers2, currQuestion.getAskDate(), currQuestion.getAnswerTimeLimit());
        List<AnswerResultResponse> publishAnswers = answerRepository.findAllPublishAnswer(currQuestion.getId());

        return PublishedExam.builder().questionToken(questionToken).challenge(challengeResponse).question(publishQuest).answers(publishAnswers).build();

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
//        UUID adminSocketId = UUID.fromString(request.getAdminSocketId());
//        socketIOServer.getClient(adminSocketId).sendEvent("studentAnswer");

        // encrypt response message
        String nextComboToken = encryptUtils.prepareComboToken(student.getUsername(), quest.getPublishedOrderNumber() + 1, comboCount);

        String encryptedResponse = encryptUtils.genEncryptedResponse(pointReceive, comboCount, quest.getEncryptKey());

        return SendAnswerResponse.builder().comboToken(nextComboToken).encryptedResponse(encryptedResponse).build();

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
