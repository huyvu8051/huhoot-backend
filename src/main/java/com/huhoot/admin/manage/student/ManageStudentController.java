package com.huhoot.admin.manage.student;

import com.huhoot.exception.UsernameExistedException;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingConverter;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("admin")
@AllArgsConstructor
public class ManageStudentController {

    private final ManageStudentService manageStudentService;

    private final VDataTablePagingConverter vDataTablePagingConverter;


    @PostMapping("/student/findAll")
    public ResponseEntity<PageResponse<StudentResponse>> getAll(@RequestBody VDataTablePagingRequest request) {
        Pageable pageable = vDataTablePagingConverter.toPageable(request);
        return ResponseEntity.ok(manageStudentService.findAllStudentAccount(pageable));
    }


    @PostMapping("/student")
    public ResponseEntity<List<StudentAddErrorResponse>> addMany(@Size(min = 1) @RequestBody List<StudentAddRequest> request) {
        return ResponseEntity.ok(manageStudentService.addManyStudentAccount(request));
    }

    @PatchMapping("/student")
    public void update(@RequestBody StudentUpdateRequest request) throws UsernameExistedException {
        manageStudentService.updateStudentAccount(request);
    }

}
