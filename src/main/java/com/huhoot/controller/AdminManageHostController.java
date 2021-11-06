package com.huhoot.controller;

import com.huhoot.dto.AdminDTO;
import com.huhoot.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminManageHostController {

    private AdminService adminService;

    @GetMapping("/host")
    public ResponseEntity<?> getAllHost(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        try {
            return ResponseEntity.ok(adminService.findAll(page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("/host/details")
    public ResponseEntity<?> getDetails(@RequestParam int id) {
        try {
            return ResponseEntity.ok(adminService.getOneDetailsById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("/host/find")
    public ResponseEntity<?> findOne(@RequestParam String username) {
        try {
            return ResponseEntity.ok(adminService.searchByUsername(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("/host")
    public ResponseEntity<?> addMany(@RequestBody List<AdminDTO> hostDTOS) {
        try {
            return ResponseEntity.ok(adminService.addMany(hostDTOS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping("/host")
    public ResponseEntity<?> update(@RequestBody @Valid AdminDTO hostDTO) {
        try {
            adminService.update(hostDTO);

            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping("/host")
    public ResponseEntity<?> lock(
            @RequestBody List<Integer> hostIds) {
        try {
            adminService.lock(hostIds);

            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

}
