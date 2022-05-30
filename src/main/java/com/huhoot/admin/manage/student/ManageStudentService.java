package com.huhoot.admin.manage.student;

import com.huhoot.dto.StudentResponse;
import com.huhoot.exception.UsernameExistedException;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManageStudentService {

    /**
     * Find all Student account
     *
     *
     * @param pageable pageable
     * @return list of student
     */
    PageResponse<StudentResponse> findAllStudentAccount(Pageable pageable);

    /**
     * @param request list of student
     * @return list of student add error
     */
    List<StudentAddErrorResponse> addManyStudentAccount(List<StudentAddRequest> request);

    /**
     * @param request student update request
     */
    void updateStudentAccount(StudentUpdateRequest request) throws UsernameExistedException;

    StudentResponse findOneByUsername(String username);

    PageResponse<ChallengeResponse> findAllChallengeParticipateIn(String username,  Pageable pageable1);

}
