package com.huhoot.admin.host;

import com.huhoot.dto.*;
import com.huhoot.exception.UsernameExistedException;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface ManageHostService {
    /**
     * Find all host account
     *
     * @param pageable
     * @return
     */
    PageResponse<HostResponse> findAllHostAccount(Pageable pageable);

    /**
     * Update host account information with FETCH
     *
     * @param hostDTO
     * @throws UsernameExistedException
     */
    void updateHostAccount(@Valid HostUpdateRequest hostDTO) throws UsernameExistedException;


    /**
     * <ul>
     * <li>Add many host account, if you want to add one account, just give it a list with one account.</li>
     * <li>It will return a list of insert error, on each error, they will contain an error message.</li>
     * </ul>
     *
     * @param hostDTOs
     * @return List of insert error
     * @throws UsernameExistedException
     */
    List<HostAddErrorResponse> addManyHostAccount(List<HostAddRequest> hostDTOs);

}
