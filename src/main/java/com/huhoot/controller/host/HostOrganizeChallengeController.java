package com.huhoot.controller.host;

import com.huhoot.dto.*;
import com.huhoot.model.Admin;
import com.huhoot.service.HostManageService;
import com.huhoot.service.HostOrganizeChallengeService;
import javassist.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/openChallenge")
    public ResponseEntity<List<StudentInChallengeResponse>> openChallenge(@RequestParam int challengeId) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostService.openChallenge(userDetails, challengeId));
    }

    @GetMapping("/prepareStudentAnswer")
    public ResponseEntity<PrepareStudentAnswerResponse> prepareStudentAnswer(@RequestParam int challengeId){
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostOrganizeChallengeService.prepareStudentAnswer(challengeId, userDetails.getId()));

    }

    @GetMapping("/studentOnline")
    public ResponseEntity<List<StudentInChallengeResponse>> updateStudentOnline(@RequestParam int challengeId) {
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

    @GetMapping("/showCorrectAnswer")
    public void showCorrectAnswer(@RequestParam int questionId, @RequestParam String challengeId) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        hostOrganizeChallengeService.showCorrectAnswer(userDetails, questionId, challengeId);
    }

    @GetMapping("/getTopStudent")
    public ResponseEntity<List<StudentScoreResponse>> getTopStudent(@RequestParam int challengeId, @RequestParam(defaultValue = "20") int size) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(0, size);
        return ResponseEntity.ok(hostOrganizeChallengeService.getTopStudent(userDetails, challengeId, pageable));
    }

    @GetMapping("/getAnswerStatistics")
    public ResponseEntity<List<AnswerStatisticsResponse>> getAnswerStatistics(@RequestParam int questionId) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostOrganizeChallengeService.getAnswerStatistics(questionId, userDetails.getId()));
    }


    @GetMapping("/endChallenge")
    public ResponseEntity<List<StudentScoreResponse>> endChallenge(@RequestParam int challengeId, @RequestParam(defaultValue = "20") int size) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostOrganizeChallengeService.endChallenge(challengeId, userDetails.getId());
        Pageable pageable = PageRequest.of(0, size);
        return ResponseEntity.ok(hostOrganizeChallengeService.getTopStudent(userDetails, challengeId, pageable));
    }


    @PostMapping("/kickStudent")
    public void kickStudent(@RequestBody KickRequest req) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostOrganizeChallengeService.kickStudent(req.getStudentIds(), req.getChallengeId(), userDetails.getId());
    }


}
