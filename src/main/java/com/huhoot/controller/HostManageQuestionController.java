package com.huhoot.controller;

import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.impl.CheckOwnerChallenge;
import com.huhoot.model.Admin;
import com.huhoot.service.HostService;
import javassist.NotFoundException;
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
@RequestMapping("host")
public class HostManageQuestionController {
    private final HostService hostService;

    private final CheckOwnerChallenge checkOwnerChallenge;

    public HostManageQuestionController(HostService hostService, CheckOwnerChallenge checkOwnerChallenge) {
        this.hostService = hostService;
        this.checkOwnerChallenge = checkOwnerChallenge;
    }

    @GetMapping("/question")
    public ResponseEntity<PageResponse<QuestionResponse>> findAll(@RequestParam int challengeId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "12") int size,
                                                                  @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                  @RequestParam(defaultValue = "DESC") String direction) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        PageResponse<QuestionResponse> response = hostService.findAllQuestionInChallenge(userDetails, challengeId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/question")
    public ResponseEntity<?> add(@Valid @RequestBody QuestionAddRequest request) throws IOException, NotFoundException, NotYourOwnException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        hostService.addOneQuestion(userDetails, request, checkOwnerChallenge);

        return ResponseEntity.ok(null);

    }

    @GetMapping("/question/details")
    public ResponseEntity<QuestionResponse> getDetails(@RequestParam int id) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.getOneOwnQuestionDetailsById(userDetails, id, checkOwnerChallenge));

    }

    @PutMapping("/question")
    public ResponseEntity<?> update(@Valid @RequestBody QuestionUpdateRequest request) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateOneQuestion(userDetails, request, checkOwnerChallenge);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/question")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.deleteManyQuestion(userDetails, ids);

        return ResponseEntity.ok(null);

    }
}
