package com.huhoot.controller.host;

import com.huhoot.dto.QuestionResponse;
import com.huhoot.dto.StudentInChallengeResponse;
import com.huhoot.model.Admin;
import com.huhoot.service.HostManageService;
import com.huhoot.service.HostOrganizeChallengeService;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("host")
public class HostOrganizeChallengeController {

    private final HostManageService hostService;

    private final HostOrganizeChallengeService hostOrganizeChallengeService;

    public HostOrganizeChallengeController(HostManageService hostService, HostOrganizeChallengeService hostOrganizeChallengeService) {
        this.hostService = hostService;
        this.hostOrganizeChallengeService = hostOrganizeChallengeService;
    }


    @GetMapping("/challenge/live")
    public ResponseEntity<List<StudentInChallengeResponse>> openChallenge(@RequestParam int challengeId) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostService.openChallenge(userDetails, challengeId));
    }

    @GetMapping("/studentOnline")
    public ResponseEntity<List<StudentInChallengeResponse>> updateStudentOnline(@RequestParam int challengeId) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostOrganizeChallengeService.getAllStudentInChallengeIsLogin(userDetails, challengeId));
    }

    @GetMapping("/startChallenge")
    public ResponseEntity<List<QuestionResponse>> startChallenge(@RequestParam int challengeId) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostOrganizeChallengeService.startChallenge(userDetails, challengeId));
    }

    @GetMapping("/publishQuestion")
    public void publishQuestion(@RequestParam int questionId) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        hostOrganizeChallengeService.publishQuestion(userDetails, questionId);
    }


}
