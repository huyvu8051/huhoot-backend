package com.huhoot.controller;

import com.huhoot.dto.HostResponse;
import com.huhoot.dto.HostAddErrorResponse;
import com.huhoot.dto.HostAddRequest;
import com.huhoot.dto.HostUpdateRequest;
import com.huhoot.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminManageHostController {

    private AdminService adminService;

    @GetMapping("/host")
    public ResponseEntity<List<HostResponse>> getAllHost(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(adminService.findAll(page, size));
    }

    @GetMapping("/host/details")
    public ResponseEntity<HostResponse> getDetails(@RequestParam int id) throws AccountNotFoundException {

        return ResponseEntity.ok(adminService.getOneDetailsById(id));

    }

    @GetMapping("/host/find")
    public ResponseEntity<List<HostResponse>> findOne(@RequestParam String username) {

        return ResponseEntity.ok(adminService.searchByUsername(username));

    }

    @PostMapping("/host")
    public ResponseEntity<List<HostAddErrorResponse>> addMany(@RequestBody List<HostAddRequest> request) {
        return ResponseEntity.ok(adminService.addMany(request));
    }

    @PutMapping("/host")
    public ResponseEntity<?> update(@RequestBody @Valid HostUpdateRequest request) {
        adminService.update(request);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/host")
    public ResponseEntity<?> lock(@RequestBody List<Integer> hostIds) {

        adminService.lock(hostIds);

        return ResponseEntity.ok(null);

    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

}
