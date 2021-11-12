package com.huhoot.controller;

import com.huhoot.dto.*;
import com.huhoot.service.AdminService;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminManageHostController {

    private final AdminService adminService;

    public AdminManageHostController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/host")
    public ResponseEntity<PageResponse<HostResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "12") int size,
                                                             @RequestParam(defaultValue = "createdDate") String sortBy,
                                                             @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        return ResponseEntity.ok(adminService.findAllHostAccount(pageable));

    }

    @GetMapping("/host/details")
    public ResponseEntity<HostResponse> getDetails(@RequestParam int id) {

        return ResponseEntity.ok(adminService.getOneHostAccountDetailsById(id));

    }

    @GetMapping("/host/search")
    public ResponseEntity<PageResponse<HostResponse>> search(@Length(min = 1, max = 15) @RequestParam String username,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "12") int size,
                                                             @RequestParam(defaultValue = "createdDate") String sortBy,
                                                             @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        return ResponseEntity.ok(adminService.searchHostAccountByUsername(username, pageable));

    }

    @PostMapping("/host")
    public ResponseEntity<List<HostAddErrorResponse>> addMany(@RequestBody List<HostAddRequest> request) {

        return ResponseEntity.ok(adminService.addManyHostAccount(request));

    }

    @PutMapping("/host")
    public ResponseEntity<?> update(@Valid @RequestBody HostUpdateRequest request) {

        adminService.updateHostAccount(request);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/host")
    public ResponseEntity<?> lock(@RequestBody List<Integer> ids) {

        adminService.lockManyHostAccount(ids);

        return ResponseEntity.ok(null);

    }


}
