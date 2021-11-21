package com.huhoot.controller;

import com.huhoot.dto.ChallengeResponse;
import com.huhoot.dto.PageResponse;
import com.huhoot.model.Admin;
import com.huhoot.model.Student;
import com.huhoot.service.StudentService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Action;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
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
