package com.huhoot.controller.student;

import com.huhoot.model.Student;
import com.huhoot.service.StudentPlayService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
