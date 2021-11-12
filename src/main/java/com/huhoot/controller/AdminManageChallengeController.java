package com.huhoot.controller;

import com.huhoot.dto.ChallengeResponse;
import com.huhoot.dto.ChallengeUpdateRequest;
import com.huhoot.dto.PageResponse;
import com.huhoot.functional.NoCheckOwnChallenge;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.model.Admin;
import com.huhoot.service.AdminService;
import com.huhoot.service.HostService;
import javassist.NotFoundException;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminManageChallengeController {

    private final AdminService adminService;

    private final HostService hostService;

    public AdminManageChallengeController(AdminService adminService, HostService hostService, NoCheckOwnChallenge noCheckOwnerChallenge) {
        this.adminService = adminService;
        this.hostService = hostService;
        this.noCheckOwnerChallenge = noCheckOwnerChallenge;
    }


    @GetMapping("/challenge")
    public ResponseEntity<PageResponse<ChallengeResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "12") int size,
                                                                   @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                   @RequestParam(defaultValue = "DESC") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        return ResponseEntity.ok(adminService.findAllChallenge(pageable));
    }

    private final NoCheckOwnChallenge noCheckOwnerChallenge;

    @GetMapping("/challenge/details")
    public ResponseEntity<ChallengeResponse> getDetails(@RequestParam int id) throws NotYourOwnException, NotFoundException, ChangeSetPersister.NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.getOneOwnChallengeDetailsById(userDetails, id, noCheckOwnerChallenge));

    }

    @GetMapping("/challenge/search")
    public ResponseEntity<PageResponse<ChallengeResponse>> search(@Length(min = 1, max = 15) @RequestParam String title,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "12") int size,
                                                                  @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                  @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(adminService.searchChallengeByTitle(userDetails, title, pageable));

    }

    @PutMapping("/challenge")
    public ResponseEntity<?> update(@Valid @RequestBody ChallengeUpdateRequest request) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateOneChallenge(userDetails, request, noCheckOwnerChallenge);

        return ResponseEntity.ok(null);
    }


    @DeleteMapping("/challenge")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.deleteManyChallenge(userDetails, ids);

        return ResponseEntity.ok(null);

    }



}
