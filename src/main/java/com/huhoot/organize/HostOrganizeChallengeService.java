package com.huhoot.organize;

import com.corundumstudio.socketio.SocketIOClient;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HostOrganizeChallengeService {

    List<StudentInChallengeResponse> openChallenge(Admin userDetails, int id) throws Exception;

    List<StudentInChallengeResponse> getAllStudentInChallengeIsLogin(Admin userDetails, int challengeId);

    /**
     * Start challenge, update challenge status to IN_PROGRESS
     *
     * @param challengeId challenge id
     * @param adminId     admin id
     * @return List of QuestionResponse
     */
    void startChallenge(int challengeId, int adminId);


    /**
     * Sent all Answer to all {@link SocketIOClient} in {@link com.corundumstudio.socketio.BroadcastOperations}
     *
     * @param questionId {@link com.huhoot.model.Question} id
     * @param adminId    {@link Admin} id
     * @throws NullPointerException not found exception
     */
    void showCorrectAnswer(int questionId, int adminId) throws NullPointerException;

    /**
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     * @param pageable    {@link Pageable}
     * @return List of top 20 student have best total challenge score
     */
    PageResponse<StudentScoreResponse> getTopStudent(int challengeId, int adminId, Pageable pageable);

    /**
     * @param adminId    {@link Admin} id
     * @param questionId {@link com.huhoot.model.Question} id
     * @param adminId    {@link Admin} id
     * @return list of answer contain number of student select
     */
    List<AnswerResultResponse> getAnswerStatistics(int adminId, int questionId);

    /**
     * Set challenge status ENDED and sent endChallenge event to all Client in Room
     *
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     * @throws NullPointerException
     */
    void endChallenge(int challengeId, int adminId) throws NullPointerException;


    /**
     * @param studentIds  List of {@link com.huhoot.model.Student} ids
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     */
    void kickStudent(List<Integer> studentIds, int challengeId, int adminId);

    void publishNextQuestion(int challengeId, Admin admin) throws Exception;

    PublishQuestionResponse getCurrentQuestion(int challengeId, int adminId) throws NullPointerException;
}