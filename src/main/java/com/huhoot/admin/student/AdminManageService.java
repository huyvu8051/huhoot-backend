package com.huhoot.admin.student;

import com.huhoot.dto.*;
import com.huhoot.exception.UsernameExistedException;
import com.huhoot.model.Admin;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.List;

public interface AdminManageService {

    /**
     * Find all Student account
     *
     *
     * @param pageable
     * @return
     */
    PageResponse<StudentResponse> findAllStudentAccount(Pageable pageable);

    /**
     * @param id
     * @return
     * @throws AccountNotFoundException
     */
    StudentResponse getOneStudentAccountDetailsById(int id) throws AccountNotFoundException;

    /**
     * @param username
     * @param isNonLocked
     * @param pageable
     * @return
     */
    PageResponse<StudentResponse> searchStudentAccountByUsername(String username, boolean isNonLocked, Pageable pageable);

    /**
     * @param request
     * @return
     */
    List<StudentAddErrorResponse> addManyStudentAccount(List<StudentAddRequest> request);

    /**
     * @param request
     */
    void updateStudentAccount(StudentUpdateRequest request) throws NotFoundException, UsernameExistedException;

    /**
     * @param hostIds
     */
    void lockManyStudentAccount(List<Integer> hostIds);

    PageResponse<ChallengeResponse> findAllChallenge(Pageable pageable);

    PageResponse<ChallengeResponse> searchChallengeByTitle(Admin userDetails, String title, Pageable pageable);

    void addStudentAccount(StudentAddRequest request) throws Exception;

}
