package com.huhoot.admin.student;

import com.huhoot.dto.*;
import com.huhoot.exception.UsernameExistedException;
import com.huhoot.vue.vdatatable.VDataTablePagingConverter;
import com.huhoot.vue.vdatatable.VDataTablePagingRequest;
import javassist.NotFoundException;
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

    private final ManageStudentService adminService;

    private final VDataTablePagingConverter vDataTablePagingConverter;


    @PostMapping("/student/findAll")
    public ResponseEntity<PageResponse<StudentResponse>> getAll(@RequestBody VDataTablePagingRequest request) {
        Pageable pageable = vDataTablePagingConverter.toPageable(request);
        return ResponseEntity.ok(adminService.findAllStudentAccount(pageable));
    }


    @PostMapping("/manyStudent")
    public ResponseEntity<List<StudentAddErrorResponse>> addMany(@Size(min = 1) @RequestBody List<StudentAddRequest> request) {
        return ResponseEntity.ok(adminService.addManyStudentAccount(request));
    }

    @PatchMapping("/student")
    public void update(@RequestBody StudentUpdateRequest request) throws NotFoundException, UsernameExistedException {
        adminService.updateStudentAccount(request);
    }

}
