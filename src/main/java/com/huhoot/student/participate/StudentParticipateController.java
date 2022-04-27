package com.huhoot.student.participate;

import com.huhoot.model.Student;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("student")
@AllArgsConstructor
public class StudentParticipateController {

    private final StudentParticipateService studentParticipateService;

    @PostMapping("/sendAnswer")
    public ResponseEntity<SendAnswerResponse> sendAnswer(@RequestBody StudentAnswerRequest request) throws Exception {

        Student userDetails = (Student) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(studentParticipateService.sendAnswer(request, userDetails));

    }

}
