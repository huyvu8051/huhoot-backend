package com.huhoot.admin.manage.challenge;

import com.huhoot.exception.NotYourOwnException;
import com.huhoot.host.manage.challenge.ChallengeAddRequest;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.host.manage.challenge.ChallengeUpdateRequest;
import com.huhoot.host.manage.challenge.ManageChallengeService;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingConverter;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("admin/challenge")
@AllArgsConstructor
public class AdminManageChallengeController {

    private final ManageChallengeService manageChallengeService;

    private final VDataTablePagingConverter vDataTablePagingConverter;

    @PostMapping("/findAll")
    public ResponseEntity<PageResponse<ChallengeResponse>> findAll(@RequestBody VDataTablePagingRequest request) {
        Pageable pageable = vDataTablePagingConverter.toPageable(request);

        return ResponseEntity.ok(manageChallengeService.findAllChallenge(pageable));
    }

    @PostMapping
    public ResponseEntity<ChallengeResponse> add(@Valid @RequestBody ChallengeAddRequest request) throws IOException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(manageChallengeService.addOneChallenge(userDetails, request));

    }

    @PatchMapping
    public void update(@Valid @RequestBody ChallengeUpdateRequest request) throws NotYourOwnException, NullPointerException {

        Challenge c = manageChallengeService.findChallenge(request.getId());

        manageChallengeService.updateOneChallenge( request, c);

    }


}
