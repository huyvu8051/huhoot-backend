package com.huhoot.controller;

import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.impl.CheckOwnerChallenge;
import com.huhoot.model.Admin;
import com.huhoot.service.HostService;
import javassist.NotFoundException;
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

@RestController
public class HostManageStudentChallenge {
    private final HostService hostService;

    private final CheckOwnerChallenge checkOwnerChallenge;

    public HostManageStudentChallenge(HostService hostService, CheckOwnerChallenge checkOwnerChallenge) {
        this.hostService = hostService;
        this.checkOwnerChallenge = checkOwnerChallenge;
    }

    @GetMapping("/challenge")
    public ResponseEntity<PageResponse<StudentInChallengeResponse>> findAll(@RequestParam int challengeId,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "12") int size,
                                                                            @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                            @RequestParam(defaultValue = "DESC") String direction) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        return ResponseEntity.ok(hostService.findAllStudentInChallenge(userDetails, pageable, challengeId));
    }


    @GetMapping("/challenge/search")
    public ResponseEntity<PageResponse<StudentInChallengeResponse>> search(@Length(min = 1, max = 15) @RequestParam int studentUsername,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "12") int size,
                                                                  @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                  @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.searchStudentInChallengeByTitle(userDetails, studentUsername, pageable));

    }

    @PostMapping("/challenge")
    public ResponseEntity<?> add(@Valid @RequestBody StudentInChallengeAddRequest request) throws IOException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        hostService.addOneStudentInChallenge(userDetails, request);

        return ResponseEntity.ok(null);

    }

    @DeleteMapping("/challenge")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.deleteManyStudentInChallenge(userDetails, ids);

        return ResponseEntity.ok(null);

    }

}
