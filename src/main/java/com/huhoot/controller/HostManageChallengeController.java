package com.huhoot.controller;

import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.impl.CheckOwnerChallenge;
import com.huhoot.model.Admin;
import com.huhoot.service.HostService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("host")
public class HostManageChallengeController {
    private HostService hostService;

    private CheckOwnerChallenge checkOwnerChallenge;

    @GetMapping("/challenge")
    public ResponseEntity<PageResponse<ChallengeResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "12") int size,
                                                                   @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                   @RequestParam(defaultValue = "DESC") String direction) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        PageResponse<ChallengeResponse> response = hostService.findAllOwnChallenge(userDetails, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/challenge/details")
    public ResponseEntity<ChallengeDetails> getDetails(@RequestParam int id) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.getOneOwnChallengeDetailsById(userDetails, id, checkOwnerChallenge));

    }

    @GetMapping("/challenge/search")
    public ResponseEntity<PageResponse<ChallengeResponse>> search(@Length(min = 1, max = 15) @RequestParam String title,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "12") int size,
                                                                  @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                  @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.searchOwnChallengeByTitle(userDetails, title, pageable));

    }

    @PostMapping("/challenge")
    public ResponseEntity<?> add(@Valid @RequestBody ChallengeAddRequest request) throws IOException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

        hostService.addOneChallenge(userDetails, request);

        return ResponseEntity.ok(null);

    }

    @PutMapping("/challenge")
    public ResponseEntity<?> update(@Valid @RequestBody ChallengeUpdateRequest request) throws NotYourOwnException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateOneChallenge(userDetails, request, checkOwnerChallenge);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/challenge")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.deleteManyChallenge(userDetails, ids);

        return ResponseEntity.ok(null);

    }

    @Autowired
    public void setHostService(HostService hostService, CheckOwnerChallenge checkOwnerChallenge) {
        this.hostService = hostService;
        this.checkOwnerChallenge = checkOwnerChallenge;
    }
}
