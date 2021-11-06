package com.huhoot.service.impl;

import com.huhoot.converter.AdminConverter;
import com.huhoot.dto.HostAddErrorResponse;
import com.huhoot.dto.HostAddRequest;
import com.huhoot.dto.HostResponse;
import com.huhoot.dto.HostUpdateRequest;
import com.huhoot.exception.UsernameExistedException;
import com.huhoot.model.Admin;
import com.huhoot.repository.AdminRepository;
import com.huhoot.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminConverter adminConverter;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Validator validator;

    @Override
    public List<HostResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Admin> admins = adminRepository.findAllByOrderByCreatedDateDesc(pageable);

        return adminConverter.toListDto(admins);
    }

    @Override
    public HostResponse getOneDetailsById(int id) throws AccountNotFoundException {
        Admin entity = adminRepository.findOneById(id);
        if (entity == null) {
            throw new AccountNotFoundException("Account not found!");
        }
        HostResponse result;
        result = adminConverter.toDto(entity);
        return result;
    }

    @Override
    public List<HostResponse> searchByUsername(String username) {
        List<HostResponse> result = new ArrayList<>();
        Admin entity = adminRepository.findOneByUsername(username);
        result.add(adminConverter.toDto(entity));
        return result;
    }

    @Override
    public void update(@Valid HostUpdateRequest hostDTO) {
        Admin host = adminRepository.findOneById(hostDTO.getId());
        String hashedPassword = passwordEncoder.encode(hostDTO.getPassword());
        host.setPassword(hashedPassword);
        host.setNonLocked(hostDTO.isNonLocked());
        adminRepository.save(host);

    }

    @Override
    public void lock(List<Integer> hostIds) {
        List<Admin> hosts = adminRepository.findAllById(hostIds);

        for (Admin e : hosts) {
            e.setNonLocked(false);
        }

        adminRepository.saveAll(hosts);

    }

    private void add(HostAddRequest hostDTO) throws UsernameExistedException {

        Set<ConstraintViolation<HostAddRequest>> violations = validator.validate(hostDTO);

        if (violations.size() > 0) {
            throw new ValidationException("Account not valid");
        }

        String formattedUsername = hostDTO.getUsername().trim().toLowerCase();

        Admin duplicate = adminRepository.findOneByUsername(formattedUsername);

        if (duplicate != null) {
            throw new UsernameExistedException("Username existed!");
        }

        String hashedPassword = passwordEncoder.encode(hostDTO.getPassword());
        Admin host = new Admin(formattedUsername, hashedPassword);
        adminRepository.save(host);
    }

    @Override
    public List<HostAddErrorResponse> addMany(List<HostAddRequest> hostDTOs) {

        List<HostAddErrorResponse> errors = new ArrayList<>();

        for (HostAddRequest hostDTO : hostDTOs) {
            try {
                this.add(hostDTO);
            } catch (Exception e) {
                HostAddErrorResponse errResponse = new HostAddErrorResponse(hostDTO);

                errResponse.setErrorMessage(e.getMessage());
                errors.add(errResponse);
                log.error(e.getMessage());
            }
        }

        return errors;


    }


}
