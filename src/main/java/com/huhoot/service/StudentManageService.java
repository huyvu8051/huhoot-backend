package com.huhoot.service;

import com.huhoot.dto.ChallengeResponse;
import com.huhoot.dto.PageResponse;
import com.huhoot.model.Student;
import org.springframework.data.domain.Pageable;

public interface StudentManageService {
    PageResponse<ChallengeResponse> findAllChallenge(Student userDetails, Pageable pageable);
}
