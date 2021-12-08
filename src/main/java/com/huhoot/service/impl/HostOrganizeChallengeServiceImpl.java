package com.huhoot.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.converter.ListConverter;
import com.huhoot.converter.StudentInChallengeConverter;
import com.huhoot.dto.*;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.model.Question;
import com.huhoot.model.StudentInChallenge;
import com.huhoot.repository.*;
import com.huhoot.service.HostOrganizeChallengeService;
import com.huhoot.utils.EncryptUtil;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class HostOrganizeChallengeServiceImpl implements HostOrganizeChallengeService {

    private final QuestionRepository questionRepository;
    private final StudentInChallengeRepository studentInChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final SocketIOServer socketIOServer;
    private final ListConverter listConverter;

    @Override
    public List<StudentInChallengeResponse> getAllStudentInChallengeIsLogin(Admin userDetails, int challengeId) {
        List<StudentInChallenge> studentInChallenges = studentInChallengeRepository.findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminIdAndIsLoginTrue(challengeId, userDetails.getId());

        return listConverter.toListResponse(studentInChallenges, StudentInChallengeConverter::toStudentChallengeResponse);

    }


    /**
     * Start challenge, update challenge status to IN_PROGRESS
     *
     * @param challengeId challenge id
     * @param adminId     admin id
     * @return List of QuestionResponse
     */
    @Override
    public void startChallenge(int challengeId, int adminId) {

        challengeRepository.updateChallengeStatusByIdAndAdminId(ChallengeStatus.IN_PROGRESS, challengeId, adminId);

        socketIOServer.getRoomOperations(challengeId + "").sendEvent("startChallenge");

    }


    private final AnswerRepository answerRepository;

    private final StudentAnswerRepository studentAnswerRepository;

    /**
     * Sent all AnswerResponse to all {@link SocketIOClient} in {@link com.corundumstudio.socketio.BroadcastOperations}
     *
     * @param questionId {@link com.huhoot.model.Question} id
     * @param adminId    {@link Admin} id
     * @throws NotFoundException not found exception
     */
    @Override
    public void showCorrectAnswer(int questionId, int adminId) throws NotFoundException {

        Optional<Integer> optional = challengeRepository.findOneByQuestionIdAndAdminId(questionId, adminId);
        Integer challengeId = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        Optional<Question> optionalQuestion = questionRepository.findOneById(questionId);

        Question question = optionalQuestion.orElseThrow(() -> new NotFoundException("Question not found"));

        List<AnswerResultResponse> answerResult = studentAnswerRepository.findAnswerStatistics(questionId, adminId);

        // question.setAskDate(null);
        // questionRepository.save(question);

        // gen key for js size
        byte[] byteKey = question.getEncryptKey();
        String keyForJS = EncryptUtil.genKeyForJsSide(byteKey);

        socketIOServer.getRoomOperations(challengeId + "").sendEvent("showCorrectAnswer", CorrectAnswerResponse.builder()
                .answers(answerResult)
                .encryptKey(keyForJS)
                .build());
    }

    /**
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     * @param pageable    {@link Pageable}
     * @return List of top 20 student have best total challenge score
     */
    @Override
    public PageResponse<StudentScoreResponse> getTopStudent(int challengeId, int adminId, Pageable pageable) {

        Page<StudentScoreResponse> response = studentAnswerRepository.findTopStudent(challengeId, adminId, pageable);

        int rankNum = 1;
        for (StudentScoreResponse studentScoreResponse : response) {
            studentScoreResponse.setRank(rankNum++);
        }

        return listConverter.toPageResponse(response);
    }

    /**
     * @param questionId {@link com.huhoot.model.Question} id
     * @param adminId    {@link Admin} id
     * @return list of answer contain number of student select
     */
    @Override
    public List<AnswerResultResponse> getAnswerStatistics(int questionId, int adminId) {
        return studentAnswerRepository.findStatisticsByQuestionId(questionId, adminId);
    }

    /**
     * Set challenge status ENDED and sent endChallenge event to all Client in Room
     *
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     * @throws NotFoundException not found
     */
    @Override
    public void endChallenge(int challengeId, int adminId) throws NotFoundException {

        Optional<Challenge> optional = challengeRepository.findOneByIdAndAdminId(challengeId, adminId);
        optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        challengeRepository.updateChallengeStatusByIdAndAdminId(ChallengeStatus.ENDED, challengeId, adminId);

        socketIOServer.getRoomOperations(challengeId + "").sendEvent("endChallenge");
    }


    /**
     * @param studentIds  List of {@link com.huhoot.model.Student} ids
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     */
    @Override
    public void kickStudent(List<Integer> studentIds, int challengeId, int adminId) {
        List<StudentInChallenge> studentInChallenges = studentInChallengeRepository.findAllByStudentIdInAndChallengeIdAndChallengeAdminId(studentIds, challengeId, adminId);


        for (StudentInChallenge sic : studentInChallenges) {
            try {
                sic.setKicked(true);
                SocketIOClient client = socketIOServer.getClient(sic.getStudent().getSocketId());
                client.sendEvent("kickStudent");
                client.disconnect();
            } catch (Exception err) {
                log.error(err.getMessage());
            }

        }


        studentInChallengeRepository.saveAll(studentInChallenges);

    }

    @Override
    public void publishNextQuestion(int challengeId, int adminId) throws Exception {
            Optional<Question> optional = questionRepository.findFirstByChallengeIdAndChallengeAdminIdAndAskDateNullOrderByOrdinalNumberAsc(challengeId, adminId);
        Question question = optional.orElseThrow(() -> new Exception("Not found or empty question"));

        int countQuestion = challengeRepository.findCountQuestion(challengeId);
        int questionOrder = questionRepository.findNumberOfPublishedQuestion(challengeId) + 1;

        PublishQuestion publishQuest = PublishQuestion.builder()
                .id(question.getId())
                .ordinalNumber(question.getOrdinalNumber())
                .questionContent(question.getQuestionContent())
                .questionImage(question.getQuestionImage())
                .answerTimeLimit(question.getAnswerTimeLimit())
                .point(question.getPoint())
                .answerOption(question.getAnswerOption())
                .challengeId(challengeId)
                .totalQuestion(countQuestion)
                .questionOrder(questionOrder)
                .theLastQuestion(countQuestion == questionOrder)
                .build();


        List<AnswerResultResponse> publishAnswers = answerRepository.findAllPublishAnswer(question.getId());




        // delay 6 sec
        long sec = 6;
        Timestamp askDate = new Timestamp(System.currentTimeMillis() + sec * 1000);
        publishQuest.setAskDate(askDate.getTime());

        socketIOServer.getRoomOperations(challengeId + "")
                .sendEvent("publishQuestion", PublishQuestionResponse.builder()
                        .question(publishQuest)
                        .answers(publishAnswers)
                        .build());

        challengeRepository.updateCurrentQuestionId(challengeId, question.getId());

        // update ask date and decryptKey
        byte[] bytes = EncryptUtil.generateRandomKeyStore();
        questionRepository.updateAskDateAndEncryptKeyByQuestionId(askDate, bytes, question.getId());


    }

    @Override
    public PublishQuestionResponse getCurrentQuestion(int challengeId, int adminId) throws NotFoundException {

        Optional<PublishQuestion> optional = challengeRepository.findCurrentPublishedQuestion(challengeId, adminId);

        PublishQuestion question = optional.orElseThrow(() -> new NotFoundException("Question  not found"));

        int questionOrder = questionRepository.findNumberOfPublishedQuestion(challengeId) + 1;

        question.setQuestionOrder(questionOrder);

        question.setTheLastQuestion(question.getTotalQuestion() == question.getQuestionOrder());

        List<AnswerResultResponse> publishAnswers = answerRepository.findAllPublishAnswer(question.getId());

        return PublishQuestionResponse.builder()
                .question(question)
                .answers(publishAnswers)
                .build();
    }


}
