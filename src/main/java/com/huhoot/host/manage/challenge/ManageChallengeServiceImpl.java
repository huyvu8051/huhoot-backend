package com.huhoot.host.manage.challenge;

import com.huhoot.converter.ListConverter;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ManageChallengeServiceImpl implements ManageChallengeService {
    private final ChallengeRepository challengeRepository;

    private final ListConverter listConverter;

    private final ChallengeMapper challengeMapper;

    @Override
    public PageResponse<ChallengeResponse> findAllChallenge(Pageable pageable) {
        Page<ChallengeResponse> challenges = challengeRepository.findAllChallengeResponse(pageable);
        return listConverter.toPageResponse(challenges);
    }

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
    public void updateOneChallenge(ChallengeUpdateRequest request, Challenge challenge) throws NullPointerException {
        challengeMapper.update(request, challenge);
        challengeRepository.save(challenge);
    }

    @Override
    public Challenge findChallengeWithOwner(int challengeId, int userId) throws NotYourOwnException {
        Challenge challenge = challengeRepository.findOneById(challengeId).orElseThrow(() -> new NullPointerException("Challenge not found"));

        if (challenge.getAdmin().getId() == userId) {
            return challenge;
        } else {
            throw new NotYourOwnException("Challenge not own");
        }

    }

    @Override
    public Challenge findChallenge(int challengeId) {
        return challengeRepository.findOneById(challengeId).orElseThrow(() -> new NullPointerException("Challenge not found"));
    }



}
