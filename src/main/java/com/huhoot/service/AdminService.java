package com.huhoot.service;

import com.huhoot.dto.*;
import com.huhoot.exception.UsernameExistedException;
import com.huhoot.model.Admin;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.List;

public interface AdminService {
    PageResponse<HostResponse> findAllHostAccount(Pageable pageable);

    void updateHostAccount(@Valid HostUpdateRequest hostDTO);

    /**
     * Lock account, account cannot log in.
     *
     * @param hostIds
     */
    void lockManyHostAccount(List<Integer> hostIds);

    /**
     * <ul>
     * <li>Add many host account, if you want to add one account, just give it a list with one account.</li>
     * <li>It will return a list of insert error, on each error, they will contain an error message.</li>
     * </ul>
     *
     * @param hostDTOS
     * @return List of insert error
     * @throws UsernameExistedException
     */
    List<HostAddErrorResponse> addManyHostAccount(List<HostAddRequest> hostDTOS);


    /**
     * Get details of one host account.
     *
     * @param id
     * @return AdminDTO
     * @throws AccountNotFoundException
     */
    HostResponse getOneHostAccountDetailsById(int id);

    /**
     * Search list of host account by username.
     *
     * @param username
     * @param pageable
     * @return AdminDTO
     */
    PageResponse<HostResponse> searchHostAccountByUsername(String username, Pageable pageable);

    /**
     * Find all Student account
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
     * @param pageable
     * @return
     */
    PageResponse<StudentResponse> searchStudentAccountByUsername(String username, Pageable pageable);

    /**
     * @param request
     * @return
     */
    List<StudentAddErrorResponse> addManyStudentAccount(List<StudentAddRequest> request);

    /**
     * @param request
     */
    void updateStudentAccount(StudentUpdateRequest request) throws NotFoundException;

    /**
     * @param hostIds
     */
    void lockManyStudentAccount(List<Integer> hostIds);

    PageResponse<ChallengeResponse> findAllChallenge(Pageable pageable);

    PageResponse<ChallengeResponse> searchChallengeByTitle(Admin userDetails, String title, Pageable pageable);
}
