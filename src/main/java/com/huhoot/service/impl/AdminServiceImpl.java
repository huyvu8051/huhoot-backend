package com.huhoot.service.impl;

import com.huhoot.converter.AdminConverter;
import com.huhoot.converter.StudentConverter;
import com.huhoot.dto.*;
import com.huhoot.exception.UsernameExistedException;
import com.huhoot.model.Admin;
import com.huhoot.model.Student;
import com.huhoot.repository.AdminRepository;
import com.huhoot.repository.StudentRepository;
import com.huhoot.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    private AdminRepository adminRepository;

    private AdminConverter adminConverter;

    private PasswordEncoder passwordEncoder;

    private StudentRepository studentRepository;

    private StudentConverter studentConverter;

    private Validator validator;


    @Override
    public PageResponse<HostResponse> findAllHostAccount(Pageable pageable) {

        Page<Admin> admins = adminRepository.findAll(pageable);

        return adminConverter.toListDto(admins);
    }

    @Override
    public HostDetailsResponse getOneHostAccountDetailsById(int id) throws AccountNotFoundException {
        Admin entity = adminRepository.findOneById(id);
        if (entity == null) {
            throw new AccountNotFoundException("Account not found!");
        }
        HostDetailsResponse result;
        result = adminConverter.toHostDetailsResponse(entity);
        return result;
    }

    @Override
    public PageResponse<HostResponse> searchHostAccountByUsername(String username, Pageable pageable) {
        Page<Admin> entities = adminRepository.findAllByUsernameContainingIgnoreCase(username, pageable);
        return adminConverter.toListDto(entities);
    }


    @Override
    public void updateHostAccount(@Valid HostUpdateRequest hostDTO) {
        Admin host = adminRepository.findOneById(hostDTO.getId());
        String hashedPassword = passwordEncoder.encode(hostDTO.getPassword());
        host.setPassword(hashedPassword);
        host.setNonLocked(hostDTO.isNonLocked());
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

    private void addOneHostAccount(HostAddRequest addRequest) throws UsernameExistedException {

        Set<ConstraintViolation<HostAddRequest>> violations = validator.validate(addRequest);

        if (violations.size() > 0) {
            throw new ValidationException("Account not valid");
        }

        String formattedUsername = addRequest.getUsername().trim().toLowerCase();

        Admin duplicate = adminRepository.findOneByUsername(formattedUsername);

        if (duplicate != null) {
            throw new UsernameExistedException("Username existed!");
        }

        String hashedPassword = passwordEncoder.encode(addRequest.getPassword());
        Admin host = new Admin(formattedUsername, hashedPassword);
        adminRepository.save(host);
    }

    @Override
    public List<HostAddErrorResponse> addManyHostAccount(List<HostAddRequest> hostDTOs) {

        List<HostAddErrorResponse> errors = new ArrayList<>();

        for (HostAddRequest hostDTO : hostDTOs) {
            try {
                this.addOneHostAccount(hostDTO);
            } catch (Exception e) {
                HostAddErrorResponse errResponse = new HostAddErrorResponse(hostDTO);

                errResponse.setErrorMessage(e.getMessage());
                errors.add(errResponse);
                log.warn(e.getMessage());
            }
        }

        return errors;


    }

    @Override
    public PageResponse<StudentResponse> findAllStudentAccount(Pageable pageable) {

        Page<Student> all = studentRepository.findAllByOrderByCreatedDateDesc(pageable);
        return studentConverter.toListStudentResponse(all);

    }

    @Override
    public StudentDetailsResponse getOneStudentAccountDetailsById(int id) throws AccountNotFoundException {
        Student entity = studentRepository.findOneById(id);

        if (entity == null) {
            throw new AccountNotFoundException("Account not found");
        }

        return studentConverter.toStudentDetailsResponse(entity);
    }

    @Override
    public PageResponse<StudentResponse> searchStudentAccountByUsername(String username, Pageable pageable) {
        Page<Student> entity = studentRepository.findAllByUsernameContainingIgnoreCase(username, pageable);
        return studentConverter.toListStudentResponse(entity);
    }

    private void addOneStudent(StudentAddRequest addRequest) throws UsernameExistedException {
        Set<ConstraintViolation<StudentAddRequest>> violations = validator.validate(addRequest);

        if (violations.size() > 0) {
            throw new ValidationException("Account not valid");
        }


        Student duplicate = studentRepository.findOneByUsername(addRequest.getUsername());

        if (duplicate != null) {
            throw new UsernameExistedException("Username existed!");
        }

        String hashedPassword = passwordEncoder.encode(addRequest.getPassword());
        Student student = new Student(addRequest.getUsername(), addRequest.getFullName(), hashedPassword);
        studentRepository.save(student);
    }

    @Override
    public List<StudentAddErrorResponse> addManyStudentAccount(List<StudentAddRequest> request) {
        List<StudentAddErrorResponse> errors = new ArrayList<>();

        for (StudentAddRequest studentAddRequest : request) {
            try {
                this.addOneStudent(studentAddRequest);
            } catch (Exception e) {
                StudentAddErrorResponse errResponse = new StudentAddErrorResponse(studentAddRequest);

                errResponse.setErrorMessage(e.getMessage());
                errors.add(errResponse);
                log.warn(e.getMessage());
            }
        }

        return errors;

    }


    @Override
    public void updateStudentAccount(StudentUpdateRequest request) {
        Student student = studentRepository.findOneById(request.getId());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        student.setPassword(hashedPassword);
        student.setFullName(request.getFullName());
        student.setNonLocked(request.isNonLocked());
        studentRepository.save(student);
    }

    @Override
    public void lockManyStudentAccount(List<Integer> ids) {
        List<Student> students = studentRepository.findAllById(ids);

        for (Student student : students) {
            student.setNonLocked(false);
        }

        studentRepository.saveAll(students);
    }

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, AdminConverter adminConverter, PasswordEncoder passwordEncoder, StudentRepository studentRepository, StudentConverter studentConverter, Validator validator) {
        this.adminRepository = adminRepository;
        this.adminConverter = adminConverter;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
        this.validator = validator;
    }

}
