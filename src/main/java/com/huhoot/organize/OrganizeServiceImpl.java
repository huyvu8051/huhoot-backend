package com.huhoot.organize;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.auth.JwtUtil;
import com.huhoot.converter.ListConverter;
import com.huhoot.converter.StudentInChallengeConverter;
import com.huhoot.encrypt.EncryptUtils;
import com.huhoot.enums.AnswerOption;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.exception.ChallengeException;
import com.huhoot.exception.NoClientInBroadcastOperations;
import com.huhoot.host.manage.challenge.ChallengeMapper;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.model.*;
import com.huhoot.repository.*;
import com.huhoot.socket.HostRegisterSuccess;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class OrganizeServiceImpl implements OrganizeService {

    private final QuestionRepository questRepo;
    private final StudentInChallengeRepository studentInChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final SocketIOServer socketIOServer;
    private final ListConverter listConverter;

    private final StudentRepository studentRepository;
    private final StudentInChallengeRepository studentChallengeRepository;
    private final JwtUtil jwtUtil;
    private final EncryptUtils encryptUtils;
    private final ChallengeMapper challengeMapper;


    @Override
    public List<StudentInChallengeResponse> getAllStudentInChallengeIsLogin(Admin userDetails, int challengeId) {
        return studentInChallengeRepository.findAllStudentIsLogin(challengeId, userDetails.getId());
    }


    /**
     * Start challenge, update challenge status to IN_PROGRESS
     *
     * @param challengeId challenge id
     * @param adminId     admin id
     */
    @Override
    public void startChallenge(int challengeId, int adminId) {

        challengeRepository.updateChallengeStatusById(ChallengeStatus.IN_PROGRESS, challengeId);

        socketIOServer.getRoomOperations(challengeId + "").sendEvent("startChallenge");

    }


    private final AnswerRepository answerRepository;

    private final StudentAnswerRepository studentAnswerRepository;

    /**
     * Sent all AnswerResponse to all {@link SocketIOClient} in {@link com.corundumstudio.socketio.BroadcastOperations}
     *
     * @throws NullPointerException not found exception
     */
    @Override
    public void showCorrectAnswer(Question question) throws NullPointerException {


        long now = System.currentTimeMillis();





        Challenge challenge = question.getChallenge();

        if (question.getTimeout() != null & question.getTimeout() > now) {
            question.setTimeout(now);
            questRepo.save(question);
        }

        List<Integer> answerResult = answerRepository.findAllCorrectAnswerIds(question.getId());


        // gen key for js size
        byte[] byteKey = question.getEncryptKey();
        String jsSideKey = encryptUtils.genKeyForJsSide(byteKey);


        int totalStudent = studentInChallengeRepository.getTotalStudentInChallenge(challenge.getId());
        int totalStudentCorrectAns = studentAnswerRepository.getTotalStudentAnswerByQuestIdAndIsCorrect(question.getId(), true).orElse(0);
        int totalStudentWrongAns = studentAnswerRepository.getTotalStudentAnswerByQuestIdAndIsCorrect(question.getId(), false).orElse(0);

        socketIOServer.getRoomOperations(challenge.getId() + "").sendEvent("showCorrectAnswer", CorrectAnswerResponse.builder().answers(answerResult).timeout(question.getTimeout()).encryptKey(jsSideKey).totalStudent(totalStudent).totalStudentCorrect(totalStudentCorrectAns).totalStudentWrong(totalStudentWrongAns).build());
    }

    /**
     * @param challengeId {@link Challenge} id
     * @param pageable    {@link Pageable}
     * @return List of top 20 student have best total challenge score
     */
    @Override
    public PageResponse<StudentScoreResponse> getTopStudent(int challengeId, Pageable pageable) {

        Page<StudentScoreResponse> response = studentAnswerRepository.findTopStudent(challengeId, pageable);

        int rankNum = 1;
        for (StudentScoreResponse studentScoreResponse : response) {
            studentScoreResponse.setRank(rankNum++);
        }

        return listConverter.toPageResponse(response);
    }


    /**
     * Set challenge status ENDED and sent endChallenge event to all Client in Room
     *
     * @param challengeId {@link Challenge} id
     * @throws NullPointerException not found
     */
    @Override
    public void endChallenge(int challengeId) throws NullPointerException {

        Optional<Challenge> optional = challengeRepository.findOneById(challengeId);
        optional.orElseThrow(() -> new NullPointerException("Challenge not found"));

        challengeRepository.updateChallengeStatusById(ChallengeStatus.ENDED, challengeId);

        socketIOServer.getRoomOperations(challengeId + "").sendEvent("endChallenge");
    }


    /**
     * @param studentIds  List of {@link com.huhoot.model.Student} ids
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     */
    @Override
    public void kickStudent(List<Integer> studentIds, int challengeId, int adminId) {
        List<StudentInChallenge> studentInChallenges = studentInChallengeRepository.findAllByStudentIdInAndChallengeIdAndAdminId(studentIds, challengeId, adminId);


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
    public void publishNextQuestion(int challengeId) throws Exception {

        Question question = questRepo.findFirstByChallengeIdAndAskDateNullOrderByOrdinalNumberAsc(challengeId).orElseThrow(() -> new Exception("Not found or empty question"));


        int countQuestion = questRepo.countQuestionInChallenge(challengeId);
        int questionOrder = questRepo.findNumberOfPublishedQuestion(challengeId) + 1;

        PublishQuestion publishQuest = PublishQuestion.builder().id(question.getId()).ordinalNumber(question.getOrdinalNumber()).questionContent(question.getQuestionContent()).questionImage(question.getQuestionImage()).answerTimeLimit(question.getAnswerTimeLimit()).point(question.getPoint()).answerOption(question.getAnswerOption()).challengeId(challengeId).totalQuestion(countQuestion).questionOrder(questionOrder).theLastQuestion(countQuestion == questionOrder).build();

        List<AnswerResultResponse> publishAnswers = answerRepository.findAllPublishAnswer(question.getId());


        // delay 6 sec
        long sec = 6;
        long askDate = System.currentTimeMillis() + sec * 1000;

        long timeout = askDate + question.getAnswerTimeLimit() * 1000;

        publishQuest.setAskDate(askDate);
        publishQuest.setTimeout(timeout);


        // update ask date and decryptKey
        questRepo.updateAskDateAndPublishedOrderNumber(askDate, timeout, questionOrder, question.getId());

        // hash correct answer ids

        List<PublishAnswer> publishAnswers2 = answerRepository.findAllAnswerByQuestionIdAndAdminId(question.getId());

        String questionToken = encryptUtils.generateQuestionToken(publishAnswers2, askDate, question.getAnswerTimeLimit());


        socketIOServer.getRoomOperations(challengeId + "").sendEvent("publishQuestion", PublishedExam.builder().questionToken(questionToken).question(publishQuest).answers(publishAnswers).build());


    }

    public PublishedExam getCurrentPublishedExam(int challengeId) {
        Optional<Question> op = questRepo.findFirstByChallengeIdAndAskDateNotNullOrderByAskDateDesc(challengeId);

        Challenge challenge = challengeRepository.findOneById(challengeId).orElseThrow(() -> new NullPointerException("Challenge not found"));
        ChallengeResponse challengeResponse = challengeMapper.toDto(challenge);
        if (!op.isPresent()) {


            return PublishedExam.builder().challenge(challengeResponse).build();
        }

        Question currQuestion = op.get();

        int countQuestion = questRepo.countQuestionInChallenge(challengeId);
        int questionOrder = questRepo.findNumberOfPublishedQuestion(challengeId) + 1;

        PublishQuestion publishQuest = PublishQuestion.builder().id(currQuestion.getId()).ordinalNumber(currQuestion.getOrdinalNumber()).askDate(currQuestion.getAskDate()).questionContent(currQuestion.getQuestionContent()).questionImage(currQuestion.getQuestionImage()).answerTimeLimit(currQuestion.getAnswerTimeLimit()).point(currQuestion.getPoint()).answerOption(currQuestion.getAnswerOption()).challengeId(challengeId).totalQuestion(countQuestion).questionOrder(questionOrder).theLastQuestion(countQuestion == questionOrder).build();

        List<PublishAnswer> publishAnswers2 = answerRepository.findAllAnswerByQuestionIdAndAdminId(currQuestion.getId());

        String questionToken = encryptUtils.generateQuestionToken(publishAnswers2, currQuestion.getAskDate(), currQuestion.getAnswerTimeLimit());
        List<AnswerResultResponse> publishAnswers = answerRepository.findAllPublishAnswer(currQuestion.getId());

        return PublishedExam.builder().questionToken(questionToken).challenge(challengeResponse).question(publishQuest).answers(publishAnswers).build();

    }


    public void findAnyClientAndEnableAutoOrganize(int challengeId) throws NoClientInBroadcastOperations {
        BroadcastOperations broadcastOperations = socketIOServer.getRoomOperations(String.valueOf(challengeId));
        broadcastOperations.sendEvent("disableAutoOrganize");
        Collection<SocketIOClient> clients = broadcastOperations.getClients();
        SocketIOClient client = clients.stream().findFirst().orElseThrow(() -> new NoClientInBroadcastOperations("no client left in this challenge"));
        client.sendEvent("enableAutoOrganize", this.getCurrentPublishedExam(challengeId));

        int id = client.get("id");
        challengeRepository.updateUserAutoOrganizeId(challengeId, id);
        challengeRepository.updateAutoOrganizeStatus(challengeId, true);
    }

    public void disableAutoOrganize(int challengeId) {
        BroadcastOperations broadcastOperations = socketIOServer.getRoomOperations(String.valueOf(challengeId));
        broadcastOperations.sendEvent("disableAutoOrganize");

        challengeRepository.updateUserAutoOrganizeId(challengeId, null);
        challengeRepository.updateAutoOrganizeStatus(challengeId, false);
    }

    @Override
    public void updateChallengeStatusToClient(int challengeId) {
        BroadcastOperations broadcastOperations = socketIOServer.getRoomOperations(String.valueOf(challengeId));
        broadcastOperations.sendEvent("updateChallengeStatus", HostRegisterSuccess.builder().totalStudentInChallenge(0).currentExam(this.getCurrentPublishedExam(challengeId)).build());
    }


    @Override
    public List<StudentInChallengeResponse> openChallenge(Admin userDetails, int challengeId) throws Exception {
        Optional<Challenge> optional = challengeRepository.findOneById(challengeId);
        Challenge challenge = optional.orElseThrow(() -> new NullPointerException("Challenge not found"));

        long t0 = System.nanoTime();
        this.createAllStudentAnswerInChallenge(challenge);
        long t1 = System.nanoTime();
        double elapsedTimeInSecond = (double) (t1 - t0) / 1_000_000_000;
        log.info("Elapsed time =" + elapsedTimeInSecond + " seconds");


        challenge.setChallengeStatus(ChallengeStatus.WAITING);
        challengeRepository.updateChallengeStatusById(ChallengeStatus.WAITING, challengeId);

        List<StudentInChallenge> studentsInChallenge = studentChallengeRepository.findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(challengeId, userDetails.getId());
        return listConverter.toListResponse(studentsInChallenge, StudentInChallengeConverter::toStudentChallengeResponse);

    }


    private void createAllStudentAnswerInChallenge(Challenge challenge) throws Exception {


        List<Student> students = studentRepository.findAllStudentInChallenge(challenge.getId());

        List<Question> questions = questRepo.findAllByChallengeId(challenge.getId());

        if (students.size() == 0 || questions.size() == 0)
            throw new ChallengeException("No student or question found in challenge id = " + challenge.getId());

        List<StudentAnswer> studentAnswers = new ArrayList<>();

        for (Question quest : questions) {
            List<Answer> answers = quest.getAnswers();
            quest.setAskDate(null);

            validateQuestion(quest, answers);

            for (Answer ans : answers) {
                for (Student stu : students) {

                    StudentAnswerId id = StudentAnswerId.builder().student(stu).answer(ans).challenge(challenge).question(quest).build();

                    studentAnswers.add(StudentAnswer.builder().primaryKey(id).score(0d).isCorrect(null).answerDate(null).build());

                }
            }
        }

        questRepo.saveAll(questions);
        studentAnswerRepository.saveAll(studentAnswers);

    }

    private void validateQuestion(Question quest, List<Answer> answers) throws ChallengeException {
        if (answers.size() == 0) throw new ChallengeException("No answer found for question id = " + quest.getId());

        boolean noAnswerCorrect = answers.stream().noneMatch(Answer::isCorrect);

        if (noAnswerCorrect) {
            throw new ChallengeException("No any answer correct for question id = " + quest.getId());
        }

        if (quest.getAnswerOption().equals(AnswerOption.SINGLE_SELECT)) {
            long answerCount = answers.stream().filter(Answer::isCorrect).count();
            if (answerCount > 1)
                throw new ChallengeException("SINGLE_SELECT: Ony one answer is correct for question id = " + quest.getId());

        }
    }


}
