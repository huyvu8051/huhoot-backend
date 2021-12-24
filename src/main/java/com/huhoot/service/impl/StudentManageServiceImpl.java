package com.huhoot.service.impl;

import com.huhoot.converter.ChallengeConverter;
import com.huhoot.converter.ListConverter;
import com.huhoot.host.manage.challenge.ChallengeResponse;
import com.huhoot.model.Challenge;
import com.huhoot.model.Student;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.StudentInChallengeRepository;
import com.huhoot.service.StudentManageService;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentManageServiceImpl implements StudentManageService {

    private final ChallengeRepository challengeRepository;

    private final StudentInChallengeRepository studentChallengeRepository;

    private final ListConverter listConverter;

    @Override
    public PageResponse<ChallengeResponse> findAllChallenge(Student userDetails, Pageable pageable) {
        List<Challenge> challenges = challengeRepository.findAllByStudentIdAndIsAvailable(userDetails.getId(), pageable);


        Page<Challenge> page = new PageImpl(challenges);

        return listConverter.toPageResponse(page, ChallengeConverter::toChallengeResponse);
    }
}
