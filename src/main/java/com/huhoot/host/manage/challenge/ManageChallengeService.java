package com.huhoot.host.manage.challenge;

import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ManageChallengeService {
    PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable);

    ChallengeResponse addOneChallenge(Admin userDetails, ChallengeAddRequest request) throws IOException;

    void updateOneChallenge(Admin userDetails, ChallengeUpdateRequest request, CheckedFunction<Admin, Challenge> biPredicate) throws NotYourOwnException, NotFoundException;

}
