package com.huhoot.controller.student;

import com.huhoot.dto.SendAnswerResponse;
import com.huhoot.dto.StudentAnswerRequest;
import com.huhoot.exception.ChallengeException;
import com.huhoot.model.Student;
import com.huhoot.service.StudentParticipateService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("student")
@AllArgsConstructor
public class StudentParticipateController {

    private final StudentParticipateService studentParticipateService;

    /**
     * May remove
     *
     * @param challengeId
     * @throws ChallengeException
     */
    @GetMapping("/joinRoom")
    public void joinRoom(@RequestParam int challengeId) throws ChallengeException {

        Student userDetails = (Student) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        // studentParticipateService.join(challengeId, userDetails.getId());

    }

    @PostMapping("/sendAnswer")
    public ResponseEntity<SendAnswerResponse> sendAnswer(@RequestBody StudentAnswerRequest request) throws Exception {

        Student userDetails = (Student) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(studentParticipateService.answer(request, userDetails));

    }

}
