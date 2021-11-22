package com.huhoot.controller.student;

import com.huhoot.dto.ChallengeResponse;
import com.huhoot.dto.PageResponse;
import com.huhoot.model.Student;
import com.huhoot.service.StudentManageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("student")
public class StudentManageController {

    private final StudentManageService studentService;

    public StudentManageController(StudentManageService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/challenge")
    public ResponseEntity<PageResponse<ChallengeResponse>> getAllChallenge(
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "12") int size,
                                        @RequestParam(defaultValue = "createdDate") String sortBy,
                                        @RequestParam(defaultValue = "DESC") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        Student userDetails = (Student) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
            return ResponseEntity.ok(studentService.findAllChallenge(userDetails, pageable));

    }
}
