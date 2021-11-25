package com.huhoot.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.converter.AnswerConverter;
import com.huhoot.converter.ListConverter;
import com.huhoot.converter.QuestionConverter;
import com.huhoot.converter.StudentInChallengeConverter;
import com.huhoot.dto.*;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.model.*;
import com.huhoot.repository.*;
import com.huhoot.service.HostOrganizeChallengeService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @Override
    public List<StudentInChallengeResponse> getAllStudentInChallengeIsLogin(Admin userDetails, int challengeId) {
        List<StudentInChallenge> studentInChallenges = studentInChallengeRepository.findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminIdAndIsLoginTrue(challengeId, userDetails.getId());

        return ListConverter.toListResponse(studentInChallenges, StudentInChallengeConverter::toStudentChallengeResponse);

    }

    @Override
    public List<QuestionResponse> startChallenge(Admin userDetails, int challengeId) throws NotFoundException {

        Optional<Challenge> optional = challengeRepository.findOneByIdAndAdminId(challengeId, userDetails.getId());
        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        challenge.setChallengeStatus(ChallengeStatus.IN_PROGRESS);
        challengeRepository.save(challenge);



        //List<Question> questions = questionRepository.findAllByChallengeIdAndChallengeAdminId(challengeId, userDetails.getId());
        //return ListConverter.toListResponse(questions, QuestionConverter::toQuestionResponse);


        log.info("===============================================================");
        List<QuestionResponse> allQuestionResponse = questionRepository.findAllQuestionResponse(challengeId, userDetails.getId());
        log.info("===============================================================");
        return allQuestionResponse;

    }

    @Override
    public void publishQuestion(Admin userDetails, int questionId) throws NotFoundException {
        Optional<Question> optional = questionRepository.findOneByIdAndChallengeAdminId(questionId, userDetails.getId());
        Question question = optional.orElseThrow(() -> new NotFoundException("Question not found"));
        int challengeId = question.getChallenge().getId();

        question.setAskDate(new Timestamp(System.currentTimeMillis()));

        PublishQuestionResponse publishQuestionResponse = QuestionConverter.toPublishQuestionResponse(question);
        socketIOServer.getRoomOperations(challengeId + "").sendEvent("publishQuestion", publishQuestionResponse);

        questionRepository.save(question);
    }


    private final AnswerRepository answerRepository;

    private final StudentAnswerRepository studentAnswerRepository;

    @Override
    public void showCorrectAnswer(Admin userDetails, int questionId, String challengeId) {

        List<Answer> answers = answerRepository.findAllByQuestionChallengeAdminIdAndQuestionId(userDetails.getId(), questionId);

        List<AnswerResponse> answerResponses = ListConverter.toListResponse(answers, AnswerConverter::toAnswerResponse);

        socketIOServer.getRoomOperations(challengeId).sendEvent("showCorrectAnswer", answerResponses);
    }


    /**
     * @param userDetails student
     * @param challengeId challenge id
     * @param pageable    pageable
     * @return List of top 20 student have best total challenge score
     */
    @Override
    public List<StudentScoreResponse> getTopStudent(Admin userDetails, int challengeId, Pageable pageable) {

        List<StudentScoreResponse> response = studentAnswerRepository.findTopStudent(challengeId, userDetails.getId(), pageable);

        int rankNum = 1;
        for (StudentScoreResponse studentScoreResponse : response) {
            studentScoreResponse.setRank(rankNum++);
        }

        return response;
    }

    /**
     * @param questionId question id
     * @param hostId     host id
     * @return list of answer contain number of student select
     */
    @Override
    public List<AnswerStatisticsResponse> getAnswerStatistics(int questionId, int hostId) {

        return studentAnswerRepository.findStatisticsByQuestionId(questionId, hostId);
    }

    @Override
    public void endChallenge(int challengeId, int adminId) throws NotFoundException {

        Optional<Challenge> optional = challengeRepository.findOneByIdAndAdminId(challengeId, adminId);

        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        socketIOServer.getRoomOperations(challengeId + "").sendEvent("endChallenge");
    }


    @Override
    public void kickStudent(List<Integer> studentIds, int challengeId, int hostId) {
        List<StudentInChallenge> studentInChallenges = studentInChallengeRepository.findAllByPrimaryKeyStudentIdInAndPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(studentIds, challengeId, hostId);


        for (StudentInChallenge sic : studentInChallenges) {
            try{
                sic.setKicked(true);
                SocketIOClient client = socketIOServer.getClient(sic.getStudent().getSocketId());
                client.sendEvent("kickStudent");
                client.disconnect();
            }catch (Exception err){
                log.error(err.getMessage());
            }

        }


        studentInChallengeRepository.saveAll(studentInChallenges);

    }

    @Override
    public PrepareStudentAnswerResponse prepareStudentAnswer(int challengeId, int id) {


        return null;
    }


}
