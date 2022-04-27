package com.huhoot.host.organize;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.converter.ListConverter;
import com.huhoot.converter.StudentInChallengeConverter;
import com.huhoot.enums.AnswerOption;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.exception.ChallengeException;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.model.*;
import com.huhoot.repository.*;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class HostOrganizeChallengeServiceImpl implements HostOrganizeChallengeService {

    private final QuestionRepository questionRepository;
    private final StudentInChallengeRepository studentInChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final SocketIOServer socketIOServer;
    private final ListConverter listConverter;

    private final StudentRepository studentRepository;
    private final StudentInChallengeRepository studentChallengeRepository;

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
     * @throws NullPointerException not found exception
     */
    @Override
    public void showCorrectAnswer(int questionId, int adminId) throws NullPointerException {

        Optional<Integer> optional = challengeRepository.findOneByQuestionIdAndAdminId(questionId, adminId);
        Integer challengeId = optional.orElseThrow(() -> new NullPointerException("Challenge not found"));

        Optional<Question> optionalQuestion = questionRepository.findOneById(questionId);

        Question question = optionalQuestion.orElseThrow(() -> new NullPointerException("Question not found"));

        List<AnswerResultResponse> answerResult = studentAnswerRepository.findAnswerStatistics(questionId, adminId);

        // question.setAskDate(null);
        // questionRepository.save(question);

        // gen key for js size
        byte[] byteKey = question.getEncryptKey();
        String keyForJS = EncryptUtil.genKeyForJsSide(byteKey);


        int totalStudent = studentInChallengeRepository.getTotalStudentInChallenge(challengeId);

        int totalStudentCorrectAns = studentAnswerRepository.getTotalStudentAnswerByQuestIdAndIsCorrect(questionId, true).orElse(0);

        int totalStudentWrongAns = studentAnswerRepository.getTotalStudentAnswerByQuestIdAndIsCorrect(questionId, false).orElse(0);

        socketIOServer.getRoomOperations(challengeId + "").sendEvent("showCorrectAnswer", CorrectAnswerResponse.builder()
                .answers(answerResult)
                .encryptKey(keyForJS)
                        .totalStudent(totalStudent)
                        .totalStudentCorrect(totalStudentCorrectAns)
                        .totalStudentWrong(totalStudentWrongAns)
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
     * @throws NullPointerException not found
     */
    @Override
    public void endChallenge(int challengeId, int adminId) throws NullPointerException {

        Optional<Challenge> optional = challengeRepository.findOneByIdAndAdminId(challengeId, adminId);
        optional.orElseThrow(() -> new NullPointerException("Challenge not found"));

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
    public void publishNextQuestion(int challengeId, Admin admin) throws Exception {
        Optional<Question> optional = questionRepository.findFirstByChallengeIdAndChallengeAdminIdAndAskDateNullOrderByOrdinalNumberAsc(challengeId, admin.getId());
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
        long askDate = System.currentTimeMillis() + sec * 1000;
        publishQuest.setAskDate(askDate);


        // update ask date and decryptKey
        questionRepository.updateAskDateAndEncryptKeyByQuestionId(askDate, question.getId());

        // hash correct answer ids
        Stream<PublishAnswer> stream = answerRepository.findAllByQuestionIdAndAdminId(question.getId(), admin.getId(), Pageable.unpaged()).stream();
        String numbersString = stream.filter(PublishAnswer::getIsCorrect).sorted(Comparator.comparingInt(PublishAnswer::getId)).map(e -> String.valueOf(e.getId()))
                .collect(Collectors.joining(""));
        String hashCorrectAnswerIds = EncryptUtil.encrypt(numbersString, question.getEncryptKey());


        socketIOServer.getRoomOperations(challengeId + "")
                .sendEvent("publishQuestion", PublishQuestionResponse.builder()
                        .question(publishQuest)
                        .answers(publishAnswers)
                        .hashCorrectAnswerIds(hashCorrectAnswerIds)
                        .adminSocketId(admin.getSocketId().toString())
                        .build());

        // update current question id
        challengeRepository.updateCurrentQuestionId(challengeId, question.getId());


    }

    @Override
    public PublishQuestionResponse getCurrentQuestion(int challengeId, int adminId) throws NullPointerException {

        Optional<PublishQuestion> optional = challengeRepository.findCurrentPublishedQuestion(challengeId, adminId);

        PublishQuestion question = optional.orElseThrow(() -> new NullPointerException("Question  not found"));

        int questionOrder = questionRepository.findNumberOfPublishedQuestion(challengeId) + 1;

        question.setQuestionOrder(questionOrder);

        question.setTheLastQuestion(question.getTotalQuestion() == question.getQuestionOrder());

        List<AnswerResultResponse> publishAnswers = answerRepository.findAllPublishAnswer(question.getId());

        return PublishQuestionResponse.builder().question(question).answers(publishAnswers).build();
    }

    @Override
    public List<StudentInChallengeResponse> openChallenge(Admin userDetails, int challengeId) throws Exception {
        Optional<Challenge> optional = challengeRepository.findOneByIdAndAdminId(challengeId, userDetails.getId());
        Challenge challenge = optional.orElseThrow(() -> new NullPointerException("Challenge not found"));

        long t0 = System.nanoTime();
        this.createAllStudentAnswerInChallenge(challenge);
        long t1 = System.nanoTime();
        double elapsedTimeInSecond = (double) (t1 - t0) / 1_000_000_000;
        log.info("Elapsed time =" + elapsedTimeInSecond + " seconds");


        challenge.setChallengeStatus(ChallengeStatus.WAITING);
        challengeRepository.updateChallengeStatusByIdAndAdminId(ChallengeStatus.WAITING, challengeId, userDetails.getId());

        List<StudentInChallenge> studentsInChallenge = studentChallengeRepository.findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(challengeId, userDetails.getId());
        return listConverter.toListResponse(studentsInChallenge, StudentInChallengeConverter::toStudentChallengeResponse);

    }


    private void createAllStudentAnswerInChallenge(Challenge challenge) throws Exception {


        List<Student> students = studentRepository.findAllStudentInChallenge(challenge.getId());

        List<Question> questions = questionRepository.findAllByChallengeId(challenge.getId());

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

        questionRepository.saveAll(questions);
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
