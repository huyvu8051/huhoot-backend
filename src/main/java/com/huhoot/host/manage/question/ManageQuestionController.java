package com.huhoot.host.manage.question;

import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.impl.CheckOwnerChallenge;
import com.huhoot.model.Admin;
import com.huhoot.service.HostManageService;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingConverter;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("host")
@AllArgsConstructor
public class ManageQuestionController {
    private final HostManageService hostService;

    private final ManageQuestionService manageQuestionService;

    private final CheckOwnerChallenge checkOwnerChallenge;

    private final VDataTablePagingConverter vDataTablePagingConverter;


    @PostMapping("/question/findAll")
    public ResponseEntity<PageResponse<QuestionResponse>> findAll(@RequestBody FindAllQuestionRequest request) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Pageable pageable = vDataTablePagingConverter.toPageable(request);
        PageResponse<QuestionResponse> response = manageQuestionService.findAllQuestionInChallenge(userDetails, request.getChallengeId(), pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/question")
    public ResponseEntity<QuestionResponse> add(@Valid @RequestBody QuestionAddRequest request) throws IOException, NotFoundException, NotYourOwnException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostService.addOneQuestion(userDetails, request, checkOwnerChallenge));

    }


    @PatchMapping("/question")
    public void update(@RequestBody QuestionUpdateRequest request) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateOneQuestion(userDetails, request, checkOwnerChallenge);

    }


}
