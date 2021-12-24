package com.huhoot.admin.manage.host;

import com.huhoot.converter.ListConverter;
import com.huhoot.exception.UsernameExistedException;
import com.huhoot.model.Admin;
import com.huhoot.repository.AdminRepository;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ManageHostServiceImpl implements ManageHostService {


    private final AdminMapper adminMapper;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    private final Validator validator;
    private final ListConverter listConverter;
    private final ManageHostRepository manageHostRepository;

    /**
     * Find all host account
     *
     * @param pageable
     * @return
     */
    @Override
    public PageResponse<HostResponse> findAllHostAccount(Pageable pageable) {

        Page<HostResponse> admins2 = manageHostRepository.findAllHost(pageable);

        return listConverter.toPageResponse(admins2);
    }

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
    @Override
    public List<HostAddErrorResponse> addManyHostAccount(List<HostAddRequest> hostDTOs) {

        List<HostAddErrorResponse> errors = new ArrayList<>();

        for (HostAddRequest hostDTO : hostDTOs) {
            try {
                this.addOneHostAccount(hostDTO);
            } catch (DataIntegrityViolationException e) {
                HostAddErrorResponse errResponse = new HostAddErrorResponse(hostDTO, "Username existed");
                errors.add(errResponse);
            } catch (Exception e) {
                HostAddErrorResponse errResponse = new HostAddErrorResponse(hostDTO, e.getMessage());
                errors.add(errResponse);
            }
        }

        return errors;


    }


    private void addOneHostAccount(HostAddRequest addRequest) {

        Set<ConstraintViolation<HostAddRequest>> violations = validator.validate(addRequest);

        if (violations.size() > 0) {

            StringBuilder sb = new StringBuilder();

            for (ConstraintViolation<HostAddRequest> violation : violations) {
                sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append(", ");
            }

            throw new ValidationException(sb.toString());
        }

        String formattedUsername = addRequest.getUsername().trim().toLowerCase();

        String hashedPassword = passwordEncoder.encode("password");
        Admin host = new Admin(formattedUsername, hashedPassword);
        adminRepository.save(host);
    }


    /**
     * Update host account information with FETCH
     *
     * @param request
     * @throws UsernameExistedException
     */
    @Override
    public void updateHostAccount(@Valid HostUpdateRequest request) throws UsernameExistedException {

        Optional<Admin> optional = adminRepository.findOneById(request.getId());

        Admin host = optional.orElseThrow(() -> new UsernameNotFoundException("Username with id: " + request.getId() + " not found"));

        if (!request.getUsername().equals(host.getUsername())) {
            Optional<Admin> duplicate = adminRepository.findOneByUsername(request.getUsername());
            if (duplicate.isPresent() && duplicate.get().getId() != request.getId())
                throw new UsernameExistedException("Username existed!");
        }

        adminMapper.update(request, host);

        adminRepository.save(host);

    }


}
