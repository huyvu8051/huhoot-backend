package com.huhoot.controller.host;

import com.huhoot.dto.*;
import com.huhoot.model.Admin;
import com.huhoot.service.HostManageService;
import com.huhoot.service.HostOrganizeChallengeService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("host")
public class HostOrganizeChallengeController {

    private final HostManageService hostService;

    private final HostOrganizeChallengeService hostOrganizeChallengeService;


    @GetMapping("/joinChallenge")
    public void joinChallenge(@RequestParam int challengeId) {
        // validate challenge
    }


    @GetMapping("/openChallenge")
    public ResponseEntity<List<StudentInChallengeResponse>> openChallenge(@RequestParam int challengeId) throws Exception {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostService.openChallenge(userDetails, challengeId));
    }

    @GetMapping("/studentOnline")
    public ResponseEntity<List<StudentInChallengeResponse>> updateStudentOnline(@RequestParam int challengeId) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostOrganizeChallengeService.getAllStudentInChallengeIsLogin(userDetails, challengeId));
    }

    /**
     * Start challenge
     *
     * @param challengeId challenge id
     * @return List of QuestionResponse
     */
    @GetMapping("/startChallenge")
    public void startChallenge(@RequestParam int challengeId) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        hostOrganizeChallengeService.startChallenge(challengeId, userDetails.getId());
    }


    /**
     * Show correct answer
     *
     * @param questionId {@link com.huhoot.model.Question} id
     * @throws NotFoundException not found
     */
    @GetMapping("/showCorrectAnswer")
    public void showCorrectAnswer(@RequestParam int questionId,
                                  @RequestParam(defaultValue = "20") int size) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostOrganizeChallengeService.showCorrectAnswer(questionId, userDetails.getId());
    }

    /**
     * @param challengeId  {@link com.huhoot.model.Challenge} id
     * @param itemsPerPage size
     * @return List of {@link StudentScoreResponse}
     */
    @GetMapping("/getTopStudent")
    public ResponseEntity<PageResponse<StudentScoreResponse>> getTopStudent(@RequestParam int challengeId,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @RequestParam(defaultValue = "20") int itemsPerPage) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        return  ResponseEntity.ok(hostOrganizeChallengeService.getTopStudent(challengeId, userDetails.getId(), pageable));
    }

    /**
     * @param questionId {@link com.huhoot.model.Question} id
     * @return List of {@link AnswerStatisticsResponse}
     */
    @GetMapping("/getAnswerStatistics")
    public ResponseEntity<List<AnswerStatisticsResponse>> getAnswerStatistics(@RequestParam int questionId) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostOrganizeChallengeService.getAnswerStatistics(questionId, userDetails.getId()));
    }


    /**
     * @param challengeId {@link com.huhoot.model.Challenge} id
     * @param size        size
     * @return List of {@link StudentScoreResponse}
     * @throws NotFoundException not found
     */
    @GetMapping("/endChallenge")
    public ResponseEntity<PageResponse<StudentScoreResponse>> endChallenge(@RequestParam int challengeId,
                                                                   @RequestParam(defaultValue = "20") int size) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostOrganizeChallengeService.endChallenge(challengeId, userDetails.getId());
        Pageable pageable = PageRequest.of(0, size);
        return ResponseEntity.ok(hostOrganizeChallengeService.getTopStudent(challengeId, userDetails.getId(), pageable));
    }


    /**
     * Kick student
     *
     * @param req {@link KickRequest}
     */
    @PostMapping("/kickStudent")
    public void kickStudent(@RequestBody KickRequest req) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostOrganizeChallengeService.kickStudent(req.getStudentIds(), req.getChallengeId(), userDetails.getId());
    }

    @GetMapping("/publishNextQuestion")
    public void publishNextQuestion(@RequestParam int challengeId) throws Exception {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        try {
            hostOrganizeChallengeService.publishNextQuestion(challengeId, userDetails.getId());
        } catch (Exception e) {
            hostOrganizeChallengeService.endChallenge(challengeId, userDetails.getId());
        }
    }


}
