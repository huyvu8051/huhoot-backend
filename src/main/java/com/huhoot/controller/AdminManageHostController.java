package com.huhoot.controller;

import com.huhoot.dto.AdminDTO;
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
    public ResponseEntity<List<AdminDTO>> getAllHost(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(adminService.findAll(page, size));
    }

    @GetMapping("/host/details")
    public ResponseEntity<AdminDTO> getDetails(@RequestParam int id) throws AccountNotFoundException {

        return ResponseEntity.ok(adminService.getOneDetailsById(id));

    }

    @GetMapping("/host/find")
    public ResponseEntity<List<AdminDTO>> findOne(@RequestParam String username) {

        return ResponseEntity.ok(adminService.searchByUsername(username));

    }

    @PostMapping("/host")
    public ResponseEntity<List<AdminDTO>> addMany(@RequestBody List<AdminDTO> hostDTOS) {
        return ResponseEntity.ok(adminService.addMany(hostDTOS));
    }

    @PutMapping("/host")
    public ResponseEntity<?> update(@RequestBody @Valid AdminDTO hostDTO) {
        adminService.update(hostDTO);

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
