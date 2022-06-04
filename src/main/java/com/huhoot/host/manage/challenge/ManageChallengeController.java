package com.huhoot.host.manage.challenge;

import com.huhoot.dto.ChallengeResponse;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.impl.CheckOwnerChallenge;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingConverter;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("host")
public class ManageChallengeController {
    private final ManageChallengeService manageChallengeService;

    private final CheckOwnerChallenge checkOwnerChallenge;

    private final VDataTablePagingConverter vDataTablePagingConverter;


    @PostMapping("/challenge/findAll")
    public ResponseEntity<PageResponse<ChallengeResponse>> findAll(@RequestBody VDataTablePagingRequest request) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Pageable pageable = vDataTablePagingConverter.toPageable(request);

        return ResponseEntity.ok(manageChallengeService.findAllOwnChallenge(userDetails.getId(), pageable));
    }


    @PostMapping("/challenge")
    public ResponseEntity<ChallengeResponse> add(@Valid @RequestBody ChallengeAddRequest request) throws IOException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(manageChallengeService.addOneChallenge(userDetails, request));

    }

    @PatchMapping("/challenge")
    public void update(@Valid @RequestBody ChallengeUpdateRequest request) throws NotYourOwnException, NullPointerException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();



        Challenge c = manageChallengeService.findChallenge(request.getId());

        manageChallengeService.updateOneChallenge(request, c);

    }


}
