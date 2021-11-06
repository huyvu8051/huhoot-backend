package com.huhoot.service;

import com.huhoot.dto.AdminDTO;
import com.huhoot.exception.UsernameExistedException;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.List;

public interface AdminService {
    List<AdminDTO> findAll(int page, int size);

    void update(@Valid AdminDTO hostDTO);

    /**
     * Lock account, account cannot log in.
     *
     * @param hostIds
     */
    void lock(List<Integer> hostIds);

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
    List<AdminDTO> addMany(List<AdminDTO> hostDTOS) throws UsernameExistedException;


    /**
     * Get details of one host account.
     *
     * @param id
     * @return AdminDTO
     * @throws AccountNotFoundException
     */
    AdminDTO getOneDetailsById(int id) throws AccountNotFoundException;

    /**
     * Search list of host account by username.
     *
     * @param username
     * @return AdminDTO
     */
    List<AdminDTO> searchByUsername(String username);
}
