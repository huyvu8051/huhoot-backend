package com.huhoot.service.impl;

import com.huhoot.converter.ChallengeConverter;
import com.huhoot.converter.ListConverter;
import com.huhoot.converter.ListConverterImpl;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.dto.PageResponse;
import com.huhoot.model.Challenge;
import com.huhoot.model.Student;
import com.huhoot.model.StudentInChallenge;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.StudentInChallengeRepository;
import com.huhoot.service.StudentManageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentManageServiceImpl implements StudentManageService {

    private final ChallengeRepository challengeRepository;

    private final StudentInChallengeRepository studentChallengeRepository;

    private final ListConverter listConverter;

    @Override
    public PageResponse<ChallengeResponse> findAllChallenge(Student userDetails, Pageable pageable) {
        List<StudentInChallenge> studentChallenges = studentChallengeRepository.findAllByPrimaryKeyStudentIdAndIsNonDeletedTrue(userDetails.getId(), pageable);

        List<Challenge> challenges = new ArrayList<>();

        for(StudentInChallenge item : studentChallenges){
            challenges.add(item.getChallenge());
        }

        Page<Challenge> page = new PageImpl(challenges);

        return listConverter.toPageResponse(page, ChallengeConverter::toChallengeResponse);
    }
}
