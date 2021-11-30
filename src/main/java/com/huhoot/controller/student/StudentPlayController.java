package com.huhoot.controller.student;

import com.huhoot.dto.StudentAnswerRequest;
import com.huhoot.model.Student;
import com.huhoot.service.StudentPlayService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("student")
public class StudentPlayController {

    private final StudentPlayService studentPlayService;

    public StudentPlayController(StudentPlayService studentPlayService) {
        this.studentPlayService = studentPlayService;
    }

    @GetMapping("/joinRoom")
    public void joinRoom(@RequestParam int challengeId) throws NotFoundException {

        Student userDetails = (Student) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        studentPlayService.join(challengeId, userDetails);

    }

    @PostMapping("/sendAnswer")
    public ResponseEntity<Integer> sendAnswer(@RequestBody StudentAnswerRequest request) throws NotFoundException {

        Student userDetails = (Student) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(studentPlayService.answer(request, userDetails));

    }

}
