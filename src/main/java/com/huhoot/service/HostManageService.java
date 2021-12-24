package com.huhoot.service;

import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.host.manage.answer.AnswerAddRequest;
import com.huhoot.host.manage.answer.AnswerUpdateRequest;
import com.huhoot.host.manage.question.QuestionAddRequest;
import com.huhoot.host.manage.question.QuestionResponse;
import com.huhoot.host.manage.question.QuestionUpdateRequest;
import com.huhoot.host.manage.studentInChallenge.StudentChallengeAddError;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeAddRequest;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeUpdateRequest;
import com.huhoot.host.organize.PublishAnswer;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HostManageService {


    QuestionResponse addOneQuestion(Admin userDetails, QuestionAddRequest request, CheckedFunction<Admin, Challenge> checker) throws NotFoundException, NotYourOwnException;

    QuestionResponse getOneOwnQuestionDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException;

    void updateOneQuestion(Admin userDetails, QuestionUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException;

    void deleteManyQuestion(Admin userDetails, List<Integer> ids);

    PageResponse<PublishAnswer> findAllAnswerByQuestionId(Admin userDetails, int questionId, Pageable pageable);

    PublishAnswer getOneAnswerDetailsById(Admin userDetails, int answerId) throws NotFoundException;

    void addOneAnswer(Admin userDetails, AnswerAddRequest request) throws Exception;

    void updateOneAnswer(Admin userDetails, AnswerUpdateRequest request) throws NotFoundException;

    void deleteManyAnswer(Admin userDetails, List<Integer> ids);

    PageResponse<StudentInChallengeResponse> findAllStudentInChallenge(Admin userDetails, Pageable pageable, int challengeId);

    PageResponse<StudentInChallengeResponse> searchStudentInChallengeByTitle(Admin userDetails, String studentUsername,int challengeId, Pageable pageable);

    List<StudentChallengeAddError> addManyStudentInChallenge(Admin userDetails, StudentInChallengeAddRequest request) throws NotFoundException;

    void updateStudentInChallenge(Admin userDetails, StudentInChallengeUpdateRequest request) throws NotFoundException;


    List<StudentInChallengeResponse> openChallenge(Admin userDetails, int id) throws Exception;


}
