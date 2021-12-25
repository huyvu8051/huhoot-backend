package com.huhoot.host.manage.studentInChallenge;

import com.huhoot.model.Admin;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManageStudentInChallengeService {

    PageResponse<StudentInChallengeResponse> findAllStudentInChallenge(Admin userDetails, Pageable pageable, int challengeId);

    PageResponse<StudentInChallengeResponse> searchStudentInChallengeByUsername(Admin userDetails, String studentUsername, int challengeId, Pageable pageable);

    List<StudentChallengeAddError> addManyStudentInChallenge(Admin userDetails, StudentInChallengeAddRequest request) throws NotFoundException;

    void updateStudentInChallenge(Admin userDetails, StudentInChallengeUpdateRequest request) throws NotFoundException;





}
