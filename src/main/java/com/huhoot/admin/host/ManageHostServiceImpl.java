package com.huhoot.admin.host;

import com.huhoot.converter.AdminConverter;
import com.huhoot.converter.ListConverter;
import com.huhoot.dto.*;
import com.huhoot.enums.Role;
import com.huhoot.exception.UsernameExistedException;
import com.huhoot.mapper.AdminMapper;
import com.huhoot.model.Admin;
import com.huhoot.repository.AdminRepository;
import lombok.AllArgsConstructor;
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
public class ManageHostServiceImpl implements ManageHostService{



    private final AdminMapper adminMapper;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    private final Validator validator;

    private final ListConverter listConverter;

    private final ManageHostRepository manageHostRepository;

    @Override
    public PageResponse<HostResponse> findAllHostAccount(Pageable pageable) {

        Page<HostResponse> admins2 = manageHostRepository.findAllHost(pageable);

        return listConverter.toPageResponse(admins2);
    }


    @Override
    public HostResponse getOneHostAccountDetailsById(int id) {
        Optional<Admin> entity = adminRepository.findOneById(id);
        return AdminConverter.toHostResponse(entity.get());
    }

    @Override
    public PageResponse<HostResponse> searchHostAccountByUsername(String username, Pageable pageable) {

        Page<Admin> entities = adminRepository.findAllByUsernameContainingIgnoreCase(username, pageable);

        return listConverter.toPageResponse(entities, AdminConverter::toHostResponse);
    }

    @Override
    public List<HostAddErrorResponse> addManyHostAccount(List<HostAddRequest> hostDTOs) {

        List<HostAddErrorResponse> errors = new ArrayList<>();

        for (HostAddRequest hostDTO : hostDTOs) {
            try {
                this.addOneHostAccount(hostDTO);
            } catch (Exception e) {
                HostAddErrorResponse errResponse = new HostAddErrorResponse(hostDTO, e.getMessage());
                errors.add(errResponse);
            }
        }

        return errors;


    }


    private void addOneHostAccount(HostAddRequest addRequest) throws UsernameExistedException {

        Set<ConstraintViolation<HostAddRequest>> violations = validator.validate(addRequest);

        if (violations.size() > 0) {

            StringBuilder sb = new StringBuilder();

            for (ConstraintViolation<HostAddRequest> violation : violations) {
                sb.append(violation.getPropertyPath());
                sb.append(" :");
                sb.append(violation.getMessage());
                sb.append(" | ");
            }

            throw new ValidationException(sb.toString());
        }

        String formattedUsername = addRequest.getUsername().trim().toLowerCase();

        /**Because of sqlite can't check the unique, so we need check it manually
         */

        Optional<Admin> duplicate = adminRepository.findOneByUsername(formattedUsername);

        if (duplicate.isPresent()) {
            throw new UsernameExistedException("Username existed!");
        }

        String hashedPassword = passwordEncoder.encode("password");
        Admin host = new Admin(formattedUsername, hashedPassword);
        adminRepository.save(host);
    }



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

    @Override
    public void lockManyHostAccount(List<Integer> ids) {
        List<Admin> hosts = adminRepository.findAllById(ids);

        for (Admin host : hosts) {
            host.setNonLocked(false);
        }

        adminRepository.saveAll(hosts);

    }
    @Override
    public void addHostAccount(HostAddRequest request) throws UsernameExistedException {
        request.setUsername(request.getUsername().trim());

        Optional<Admin> duplicate = adminRepository.findOneByUsername(request.getUsername());
        if (duplicate.isPresent()) throw new UsernameExistedException("Username existed!");


        Admin host = adminMapper.toEntity(request);

        host.setPassword(passwordEncoder.encode("password"));

        host.setRole(Role.HOST);

        adminRepository.save(host);
    }
}
