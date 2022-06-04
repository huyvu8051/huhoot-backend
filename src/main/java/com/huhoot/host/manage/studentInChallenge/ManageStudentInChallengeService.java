package com.huhoot.host.manage.studentInChallenge;

import com.huhoot.dto.StudentAnswerResult;
import com.huhoot.model.Admin;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManageStudentInChallengeService {

    PageResponse<StudentInChallengeResponse> findAllStudentInChallenge(int challengeId, Pageable pageable);

    PageResponse<StudentInChallengeResponse> searchStudentInChallengeByUsername(Admin userDetails, String studentUsername, int challengeId, Pageable pageable);

    List<StudentChallengeAddError> addManyStudentInChallenge(Admin userDetails, StudentInChallengeAddRequest request) throws NullPointerException;

    void updateStudentInChallenge(Admin userDetails, StudentInChallengeUpdateRequest request) throws NullPointerException;


    PageResponse<StudentInChallengeResponse> findAllParticipants(int challengeId, Pageable pageable1);

    List<StudentAnswerResult> findAllStudentAnswerResult(List<Integer> questionIds, int studentId);
}
