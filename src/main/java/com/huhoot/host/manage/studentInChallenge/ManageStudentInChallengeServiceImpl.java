package com.huhoot.host.manage.studentInChallenge;

import com.huhoot.converter.ListConverter;
import com.huhoot.converter.StudentInChallengeConverter;
import com.huhoot.host.manage.studentInChallenge.StudentChallengeAddError;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeAddRequest;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeUpdateRequest;
import com.huhoot.mapper.StudentInChallengeMapper;
import com.huhoot.model.*;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.StudentInChallengeRepository;
import com.huhoot.repository.StudentRepository;
import com.huhoot.host.manage.studentInChallenge.ManageStudentInChallengeService;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ManageStudentInChallengeServiceImpl implements ManageStudentInChallengeService {
    private final ChallengeRepository challengeRepository;

    private final ListConverter listConverter;

    private final StudentInChallengeRepository studentChallengeRepository;

    private final StudentInChallengeMapper studentInChallengeMapper;

    private final StudentRepository studentRepository;
    @Override
    public PageResponse<StudentInChallengeResponse> findAllStudentInChallenge(Admin userDetails, Pageable pageable, int challengeId) {
        Page<StudentInChallengeResponse> page = studentChallengeRepository.findAllByChallengeIdAndAdminId(challengeId, userDetails.getId(), pageable);
        return listConverter.toPageResponse(page);
    }

    @Override
    public PageResponse<StudentInChallengeResponse> searchStudentInChallengeByUsername(Admin userDetails, String studentUsername, int challengeId, Pageable pageable) {
        Page<StudentInChallenge> page = studentChallengeRepository.findAllByPrimaryKeyStudentUsernameContainingIgnoreCaseAndPrimaryKeyChallengeId(studentUsername, challengeId, pageable);
        return listConverter.toPageResponse(page, StudentInChallengeConverter::toStudentChallengeResponse);
    }


    /**
     *
     */
    @Override
    public List<StudentChallengeAddError> addManyStudentInChallenge(Admin userDetails, StudentInChallengeAddRequest request) throws NotFoundException {
        Optional<Challenge> optional = challengeRepository.findOneById(request.getChallengeId());

        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        List<StudentChallengeAddError> errors = new ArrayList<>();

        for (int id : request.getStudentIds()) {
            try {
                Optional<Student> optionalStudent = studentRepository.findOneById(id);
                Student student = optionalStudent.orElseThrow(() -> new NotFoundException("Student not found"));

                studentChallengeRepository.save(new StudentInChallenge(student, challenge));
            } catch (Exception e) {
                errors.add(new StudentChallengeAddError(id, e.getMessage()));
            }
        }

        return errors;

    }


    @Override
    public void updateStudentInChallenge(Admin userDetails, StudentInChallengeUpdateRequest request) throws NotFoundException {

        Optional<StudentInChallenge> optional = studentChallengeRepository.findOneByPrimaryKeyStudentIdAndPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(request.getStudentId(), request.getChallengeId(), userDetails.getId());

        StudentInChallenge studentInChallenge = optional.orElseThrow(() -> new NotFoundException("Student in challenge not found"));

        studentInChallengeMapper.update(request, studentInChallenge);

        studentChallengeRepository.save(studentInChallenge);

    }





}
