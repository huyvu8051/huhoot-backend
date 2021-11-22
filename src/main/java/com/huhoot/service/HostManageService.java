package com.huhoot.service;

import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface HostManageService {
    PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable);

    ChallengeResponse getOneOwnChallengeDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException;

    PageResponse<ChallengeResponse> searchOwnChallengeByTitle(Admin userDetails, String title, Pageable pageable);

    void addOneChallenge(Admin userDetails, ChallengeAddRequest request) throws IOException;

    void updateOneChallenge(Admin userDetails, ChallengeUpdateRequest request, CheckedFunction<Admin, Challenge> biPredicate) throws NotYourOwnException, NotFoundException;

    void deleteManyChallenge(Admin userDetails, List<Integer> ids);

    PageResponse<QuestionResponse> findAllQuestionInChallenge(Admin userDetails, int challengeId, Pageable pageable);

    void addOneQuestion(Admin userDetails, QuestionAddRequest request, CheckedFunction<Admin, Challenge> checker) throws NotFoundException, NotYourOwnException;

    QuestionResponse getOneOwnQuestionDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException;

    void updateOneQuestion(Admin userDetails, QuestionUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException;

    void deleteManyQuestion(Admin userDetails, List<Integer> ids);

    List<AnswerResponse> findAllAnswerByQuestionId(Admin userDetails, int questionId);

    AnswerResponse getOneAnswerDetailsById(Admin userDetails, int answerId) throws NotFoundException;

    void addOneAnswer(Admin userDetails, AnswerAddRequest request) throws Exception;

    void updateOneAnswer(Admin userDetails, AnswerUpdateRequest request) throws NotFoundException;

    void deleteManyAnswer(Admin userDetails, List<Integer> ids);

    PageResponse<StudentInChallengeResponse> findAllStudentInChallenge(Admin userDetails, Pageable pageable, int challengeId);

    PageResponse<StudentInChallengeResponse> searchStudentInChallengeByTitle(Admin userDetails, String studentUsername,int challengeId, Pageable pageable);

    List<StudentChallengeAddError> addManyStudentInChallenge(Admin userDetails, StudentInChallengeAddRequest request) throws NotFoundException;

    void deleteManyStudentInChallenge(Admin userDetails, StudentInChallengeDeleteRequest request);


    List<StudentInChallengeResponse> openChallenge(Admin userDetails, int id) throws NotFoundException;


}
