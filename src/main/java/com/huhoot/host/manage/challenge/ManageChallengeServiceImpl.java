package com.huhoot.host.manage.challenge;

import com.huhoot.converter.ListConverter;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ManageChallengeServiceImpl implements ManageChallengeService {
    private final ChallengeRepository challengeRepository;

    private final ListConverter listConverter;

    private final ChallengeMapper challengeMapper;

    @Override
    public PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable) {
        Page<ChallengeResponse> challenges = challengeRepository.findAllByAdminId(userDetails.getId(), pageable);
        return listConverter.toPageResponse(challenges);
    }


    @Override
    public ChallengeResponse addOneChallenge(Admin userDetails, ChallengeAddRequest request) {

        Challenge challenge = challengeMapper.toEntity(request);
        challenge.setAdmin(userDetails);

        Challenge saved = challengeRepository.save(challenge);

        return challengeMapper.toDto(saved);

    }


    @Override
    public void updateOneChallenge(Admin userDetails, ChallengeUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NullPointerException {
        Optional<Challenge> optional = challengeRepository.findOneById(request.getId());
        Challenge challenge = optional.orElseThrow(() -> new NullPointerException("Challenge not found"));
        checker.accept(userDetails, challenge);
        challengeMapper.update(request, challenge);
        challengeRepository.save(challenge);

    }


}
