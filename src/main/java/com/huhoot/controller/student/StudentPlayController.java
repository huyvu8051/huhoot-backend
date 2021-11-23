package com.huhoot.controller.t;

import com.huhoot.dto.StudentAnswerRequest;
import com.huhoot.model.Student;
import com.huhoot.service.StudentPlayService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("student")
public class StudentPlayController {

    private final StudentPlayService studentPlayService;

    public StudentPlayController(StudentPlayService studentPlayService) {
        this.studentPlayService = studentPlayService;
    }

    @PostMapping("/challenge")
    public void joinRoom(@RequestBody int challengeId) throws NotFoundException {

        Student userDetails = (Student) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        studentPlayService.join(challengeId, userDetails);

    }

    @PostMapping("/sendAnswer")
    public ResponseEntity<Integer> joinRoom(@RequestBody StudentAnswerRequest request) throws NotFoundException {

        Student userDetails = (Student) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(studentPlayService.answer(request, userDetails));

    }

}
