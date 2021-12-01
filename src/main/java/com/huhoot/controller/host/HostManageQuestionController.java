package com.huhoot.controller.host;

import com.huhoot.dto.PageResponse;
import com.huhoot.dto.QuestionAddRequest;
import com.huhoot.dto.QuestionResponse;
import com.huhoot.dto.QuestionUpdateRequest;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.impl.CheckOwnerChallenge;
import com.huhoot.model.Admin;
import com.huhoot.service.HostManageService;
import javassist.NotFoundException;
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
    private final HostManageService hostService;

    private final CheckOwnerChallenge checkOwnerChallenge;

    public HostManageQuestionController(HostManageService hostService, CheckOwnerChallenge checkOwnerChallenge) {
        this.hostService = hostService;
        this.checkOwnerChallenge = checkOwnerChallenge;
    }

    @GetMapping("/question")
    public ResponseEntity<PageResponse<QuestionResponse>> findAll(@RequestParam int challengeId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "12") int itemsPerPage,
                                                                  @RequestParam(defaultValue = "ordinalNumber") String sortBy,
                                                                  @RequestParam(defaultValue = "ASC") String direction) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.Direction.fromString(direction), sortBy);
        PageResponse<QuestionResponse> response = hostService.findAllQuestionInChallenge(userDetails, challengeId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/question")
    public ResponseEntity<QuestionResponse> add(@Valid @RequestBody QuestionAddRequest request) throws IOException, NotFoundException, NotYourOwnException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostService.addOneQuestion(userDetails, request, checkOwnerChallenge));

    }

    @GetMapping("/question/details")
    public ResponseEntity<QuestionResponse> getDetails(@RequestParam int id) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.getOneOwnQuestionDetailsById(userDetails, id, checkOwnerChallenge));

    }

    @PatchMapping("/question")
    public void update(@RequestBody QuestionUpdateRequest request) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateOneQuestion(userDetails, request, checkOwnerChallenge);

    }

    @DeleteMapping("/question")
    public void delete(@RequestBody List<Integer> ids) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.deleteManyQuestion(userDetails, ids);

    }
}
