package com.huhoot.service.impl;

import com.huhoot.converter.ChallengeConverter;
import com.huhoot.converter.ListConverter;
import com.huhoot.converter.StudentConverter;
import com.huhoot.dto.*;
import com.huhoot.exception.UsernameExistedException;
import com.huhoot.mapper.StudentMapper;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.model.Student;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.StudentRepository;
import com.huhoot.admin.student.AdminManageService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class AdminManageServiceImpl implements AdminManageService {
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final Validator validator;




    private final ListConverter listConverter;


    private final StudentMapper studentMapper;

    @Override
    public PageResponse<StudentResponse> findAllStudentAccount(Pageable pageable) {

        Page<Student> all = studentRepository.findAll(pageable);

        return listConverter.toPageResponse(all, e -> studentMapper.toDto(e));

    }

    @Override
    public StudentResponse getOneStudentAccountDetailsById(int id) throws AccountNotFoundException {
        Optional<Student> optional = studentRepository.findOneById(id);

        Student student = optional.orElseThrow(() -> new AccountNotFoundException("Account not found"));

        return StudentConverter.toStudentResponse(student);
    }

    @Override
    public PageResponse<StudentResponse> searchStudentAccountByUsername(String username, boolean isNonLocked, Pageable pageable) {
        Page<Student> entity = studentRepository.findAllByUsernameContainingIgnoreCaseAndIsNonLocked(username, isNonLocked, pageable);
        return listConverter.toPageResponse(entity, StudentConverter::toStudentResponse);
    }

    private void addOneStudent(StudentAddRequest addRequest) throws UsernameExistedException {
        Set<ConstraintViolation<StudentAddRequest>> violations = validator.validate(addRequest);

        if (violations.size() > 0) {
            StringBuilder sb = new StringBuilder();

            for (ConstraintViolation<StudentAddRequest> violation : violations) {
                sb.append(violation.getPropertyPath());
                sb.append(" :");
                sb.append(violation.getMessage());
                sb.append(" | ");
            }

            throw new ValidationException(sb.toString());
        }


        Optional<Student> duplicate = studentRepository.findOneByUsername(addRequest.getUsername());

        if (duplicate.isPresent()) {
            throw new UsernameExistedException("Username existed!");
        }

        String hashedPassword = passwordEncoder.encode("password");
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
    public void updateStudentAccount(StudentUpdateRequest request) throws NotFoundException, UsernameExistedException {

        Optional<Student> duplicate = studentRepository.findOneByUsername(request.getUsername());

        if (duplicate.isPresent() && !duplicate.get().getUsername().equals(request.getUsername())) {
            throw new UsernameExistedException("Username existed!");
        }

        Optional<Student> optional = studentRepository.findOneById(request.getId());

        Student entity = optional.orElseThrow(() -> new NotFoundException("Student not found"));

        studentMapper.update(request, entity);

        studentRepository.save(entity);
    }

    @Override
    public void lockManyStudentAccount(List<Integer> ids) {
        List<Student> students = studentRepository.findAllById(ids);

        for (Student student : students) {
            student.setNonLocked(false);
        }

        studentRepository.saveAll(students);
    }

    private final ChallengeRepository challengeRepository;

    @Override
    public PageResponse<ChallengeResponse> findAllChallenge(Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAll(pageable);
        return listConverter.toPageResponse(challenges, ChallengeConverter::toChallengeResponse);
    }

    @Override
    public PageResponse<ChallengeResponse> searchChallengeByTitle(Admin userDetails, String title, Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAllByTitleContainingIgnoreCase(title, pageable);
        return listConverter.toPageResponse(challenges, ChallengeConverter::toChallengeResponse);
    }

    @Override
    public void addStudentAccount(StudentAddRequest request) throws Exception {

        request.setUsername(request.getUsername().trim());

        Optional<Student> duplicate = studentRepository.findOneByUsername(request.getUsername());

        if (duplicate.isPresent()) {
            throw new UsernameExistedException("Username existed!");
        }

        Student student = studentMapper.toEntity(request);

        student.setPassword(passwordEncoder.encode("password"));

        studentRepository.save(student);


    }



}
