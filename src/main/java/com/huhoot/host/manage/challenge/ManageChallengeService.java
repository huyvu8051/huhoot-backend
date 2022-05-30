package com.huhoot.host.manage.challenge;

import com.huhoot.dto.ChallengeResponse;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ManageChallengeService {

    PageResponse<ChallengeResponse> findAllChallenge(Pageable pageable);

    PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable);

    ChallengeResponse addOneChallenge(Admin userDetails, ChallengeAddRequest request) throws IOException;

    void updateOneChallenge(ChallengeUpdateRequest request, Challenge challenge) throws NotYourOwnException, NullPointerException;

    Challenge findChallengeWithOwner(int id, int id1) throws NotYourOwnException;

    Challenge findChallenge(int challengeId);


}
