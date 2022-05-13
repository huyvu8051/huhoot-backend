package com.huhoot.host.manage.answer;

import com.huhoot.organize.PublishAnswer;
import com.huhoot.model.Admin;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("host")
@AllArgsConstructor
public class HostManageAnswerController {

    private final ManageAnswerService manageAnswerService;
    private final VDataTablePagingConverter vDataTablePagingConverter;

    @PostMapping("/answer/findAll")
    public ResponseEntity<PageResponse<PublishAnswer>> findAll(@RequestBody FindAllAnswerRequest request) {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Pageable pageable = vDataTablePagingConverter.toPageable(request);

        return ResponseEntity.ok(manageAnswerService.findAllAnswerByQuestionId(userDetails, request.getQuestionId(), pageable));

    }

    @PostMapping("/answer")
    public void add(@Valid @RequestBody AnswerAddRequest request) throws Exception {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        manageAnswerService.addOneAnswer(userDetails, request);

    }

    @PatchMapping("/answer")
    public void update(@Valid @RequestBody AnswerUpdateRequest request) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        manageAnswerService.updateOneAnswer(userDetails, request);
    }

    @PatchMapping("/answer/ordinal")
    public void updateOrdinal(@Valid @RequestBody AnswerOrdinalUpdateRequest request) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        manageAnswerService.updateOrdinal(userDetails, request);
    }

}
