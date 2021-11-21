package com.huhoot.service.impl;

import com.huhoot.converter.ChallengeConverter;
import com.huhoot.converter.ListConverter;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.dto.PageResponse;
import com.huhoot.model.Challenge;
import com.huhoot.model.Student;
import com.huhoot.model.StudentInChallenge;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.StudentChallengeRepository;
import com.huhoot.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final ChallengeRepository challengeRepository;

    private final StudentChallengeRepository studentChallengeRepository;

    public StudentServiceImpl(ChallengeRepository challengeRepository, StudentChallengeRepository studentChallengeRepository) {
        this.challengeRepository = challengeRepository;
        this.studentChallengeRepository = studentChallengeRepository;
    }


    @Override
    public PageResponse<ChallengeResponse> findAllChallenge(Student userDetails, Pageable pageable) {
        List<StudentInChallenge> studentChallenges = studentChallengeRepository.findAllByPrimaryKeyStudentIdAndIsNonDeletedFalse(userDetails.getId(), pageable);

        List<Challenge> challenges = new ArrayList<>();

        for(StudentInChallenge item : studentChallenges){
            challenges.add(item.getChallenge());
        }

        Page<Challenge> page = new PageImpl(challenges);

        return ListConverter.toPageResponse(page, ChallengeConverter::toChallengeResponse);
    }
}
