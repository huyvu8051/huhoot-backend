package com.huhoot.service;

import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.functional.impl.CheckOwnerChallenge;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface HostService {
    PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable);

    ChallengeDetails getOneOwnChallengeDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException;

    PageResponse<ChallengeResponse> searchOwnChallengeByTitle(Admin userDetails, String title, Pageable pageable);

    void addOneChallenge(Admin userDetails, ChallengeAddRequest request) throws IOException;

    void updateOneChallenge(Admin userDetails, ChallengeUpdateRequest request, CheckedFunction<Admin, Challenge> biPredicate) throws NotYourOwnException;

    void deleteManyChallenge(Admin userDetails, List<Integer> ids);

    PageResponse<QuestionResponse> findAllQuestionInChallenge(Admin userDetails, int challengeId, Pageable pageable);

    void addOneQuestion(Admin userDetails, QuestionAddRequest request, CheckedFunction<Admin, Challenge> checker) throws NotFoundException, NotYourOwnException;

    QuestionDetails getOneOwnQuestionDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException;

    void updateOneQuestion(Admin userDetails, QuestionUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException;

    void deleteManyQuestion(Admin userDetails, List<Integer> ids);
}
