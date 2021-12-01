package com.huhoot.controller.host;

import com.huhoot.dto.*;
import com.huhoot.functional.impl.CheckOwnerChallenge;
import com.huhoot.model.Admin;
import com.huhoot.service.AdminManageService;
import com.huhoot.service.HostManageService;
import javassist.NotFoundException;
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
public class HostManageStudentChallengeController {
    private final HostManageService hostService;

    private final CheckOwnerChallenge checkOwnerChallenge;

    private final AdminManageService adminService;



    @GetMapping("/student")
    public ResponseEntity<PageResponse<StudentResponse>> findALlStudent(@RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "12") int itemsPerPage,
                                                                        @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                        @RequestParam(defaultValue = "DESC") String direction) {
        Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.Direction.fromString(direction), sortBy);
        return ResponseEntity.ok(adminService.findAllStudentAccount(pageable));
    }

    @GetMapping("/student/search")
    public ResponseEntity<PageResponse<StudentResponse>> search(@Length(min = 1, max = 10)
                                                                @RequestParam String username,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "12") int itemsPerPage,
                                                                @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                @RequestParam(defaultValue = "DESC") String direction,
                                                                @RequestParam(defaultValue = "true") boolean isNonLocked) {
        Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.Direction.fromString(direction), sortBy);
        return ResponseEntity.ok(adminService.searchStudentAccountByUsername(username, isNonLocked, pageable));

    }


    @GetMapping("/studentChallenge")
    public ResponseEntity<PageResponse<StudentInChallengeResponse>> findAll(@RequestParam int challengeId,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @RequestParam(defaultValue = "12") int itemsPerPage,
                                                                            @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                            @RequestParam(defaultValue = "DESC") String direction) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.Direction.fromString(direction), sortBy);

        return ResponseEntity.ok(hostService.findAllStudentInChallenge(userDetails, pageable, challengeId));
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
        return ResponseEntity.ok(hostService.searchStudentInChallengeByTitle(userDetails, studentUsername, challengeId, pageable));

    }

    @PostMapping("/studentChallenge")
    public ResponseEntity<List<StudentChallengeAddError>> add(@Valid @RequestBody StudentInChallengeAddRequest request) throws IOException, NotFoundException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();


        return ResponseEntity.ok(hostService.addManyStudentInChallenge(userDetails, request));

    }


    @PatchMapping("/studentChallenge")
    public void update(@RequestBody StudentInChallengeUpdateRequest request) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateStudentInChallenge(userDetails, request);

    }

}
