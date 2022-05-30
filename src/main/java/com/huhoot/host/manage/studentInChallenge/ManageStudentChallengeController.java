package com.huhoot.host.manage.studentInChallenge;

import com.huhoot.admin.manage.student.ManageStudentService;
import com.huhoot.dto.StudentResponse;
import com.huhoot.host.manage.question.FindAllQuestionRequest;
import com.huhoot.model.Admin;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingConverter;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
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
@AllArgsConstructor
public class ManageStudentChallengeController {
    private final ManageStudentInChallengeService hostService;

    private final ManageStudentService manageStudentService;
    private final VDataTablePagingConverter vDataTablePagingConverter;

    @PostMapping("/student")
    public ResponseEntity<PageResponse<StudentResponse>> findAllStudent(@RequestBody VDataTablePagingRequest request) {
        Pageable pageable = vDataTablePagingConverter.toPageable(request);
        return ResponseEntity.ok(manageStudentService.findAllStudentAccount(pageable));
    }


    @PostMapping("/studentChallenge/findAll")
    public ResponseEntity<PageResponse<StudentInChallengeResponse>> findAll(@RequestBody FindAllQuestionRequest request) {

        Pageable pageable = vDataTablePagingConverter.toPageable(request);

        return ResponseEntity.ok(hostService.findAllStudentInChallenge(request.getChallengeId(), pageable));
    }


    @GetMapping("/studentChallenge/search")
    public ResponseEntity<PageResponse<StudentInChallengeResponse>> search(@Length(min = 1, max = 15) @RequestParam String studentUsername,
                                                                           @RequestParam int challengeId,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "12") int size,
                                                                           @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                           @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.searchStudentInChallengeByUsername(userDetails, studentUsername, challengeId, pageable));

    }

    @PostMapping("/studentChallenge/add")
    public ResponseEntity<List<StudentChallengeAddError>> add(@Valid @RequestBody StudentInChallengeAddRequest request) throws IOException, NullPointerException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();


        return ResponseEntity.ok(hostService.addManyStudentInChallenge(userDetails, request));

    }


    @PatchMapping("/studentChallenge")
    public void update(@RequestBody StudentInChallengeUpdateRequest request) throws NullPointerException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateStudentInChallenge(userDetails, request);

    }

}
