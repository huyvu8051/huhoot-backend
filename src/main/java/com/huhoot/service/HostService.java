package com.huhoot.service;

import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.model.Admin;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public interface HostService {
    PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable);

    ChallengeDetails getOneOwnChallengeDetailsById(Admin userDetails, int id) throws NotYourOwnException, NotFoundException;

    PageResponse<ChallengeResponse> searchOwnChallengeByTitle(Admin userDetails, String title, Pageable pageable);

    void addOneChallenge(Admin userDetails ,ChallengeAddRequest request);

    void updateOneChallenge(Admin userDetails,ChallengeUpdateRequest request, BiPredicate<Integer, Integer> biPredicate) throws NotYourOwnException;
}
