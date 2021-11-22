package com.huhoot.controller.admin;

import com.huhoot.dto.*;
import com.huhoot.service.AdminManageService;
import javassist.NotFoundException;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminManageStudentController {

    private final AdminManageService adminService;

    public AdminManageStudentController(AdminManageService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/student")
    public ResponseEntity<PageResponse<StudentResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "12") int size,
                                                                @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                @RequestParam(defaultValue = "DESC") String direction,
                                                                @RequestParam(defaultValue = "true") boolean isNonLocked) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        return ResponseEntity.ok(adminService.findAllStudentAccount(isNonLocked, pageable));
    }

    @GetMapping("/student/details")
    public ResponseEntity<StudentResponse> getDetails(@RequestParam int id) throws AccountNotFoundException {

        return ResponseEntity.ok(adminService.getOneStudentAccountDetailsById(id));
    }

    @GetMapping("/student/search")
    public ResponseEntity<PageResponse<StudentResponse>> search(@Length(min = 1, max = 10) @RequestParam String username,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "12") int size,
                                                                @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                @RequestParam(defaultValue = "DESC") String direction,
                                                                @RequestParam(defaultValue = "true") boolean isNonLocked) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        return ResponseEntity.ok(adminService.searchStudentAccountByUsername(username, isNonLocked, pageable));

    }

    @PostMapping("/student")
    public ResponseEntity<List<StudentAddErrorResponse>> addMany(@Size(min = 1) @RequestBody List<StudentAddRequest> request) {
        return ResponseEntity.ok(adminService.addManyStudentAccount(request));
    }

    @PutMapping("/student")
    public void update(@Valid @RequestBody StudentUpdateRequest request) throws NotFoundException {
        adminService.updateStudentAccount(request);
    }

    @DeleteMapping("/student")
    public void lock(@RequestBody List<Integer> ids) {

        adminService.lockManyStudentAccount(ids);

    }

}
