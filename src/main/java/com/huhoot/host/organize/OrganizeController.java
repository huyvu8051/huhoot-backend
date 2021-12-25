package com.huhoot.host.organize;

import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.model.Admin;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController("hostOrganizeController")
@RequestMapping("host")
public class OrganizeController {


    private final HostOrganizeChallengeService hostOrganizeChallengeService;

    @GetMapping("/openChallenge")
    public ResponseEntity<List<StudentInChallengeResponse>> openChallenge(@RequestParam int challengeId) throws Exception {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostOrganizeChallengeService.openChallenge(userDetails, challengeId));
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
        return ResponseEntity.ok(hostOrganizeChallengeService.getTopStudent(challengeId, userDetails.getId(), pageable));
    }

    /**
     * @param questionId {@link com.huhoot.model.Question} id
     * @return List of {@link AnswerResultResponse}
     */
    @GetMapping("/getAnswerStatistics")
    public ResponseEntity<List<AnswerResultResponse>> getAnswerStatistics(@RequestParam int questionId) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostOrganizeChallengeService.getAnswerStatistics(questionId, userDetails.getId()));
    }


    /**
     * @param challengeId {@link com.huhoot.model.Challenge} id
     * @return List of {@link StudentScoreResponse}
     * @throws NotFoundException not found
     */
    @GetMapping("/endChallenge")
    public void endChallenge(@RequestParam int challengeId) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostOrganizeChallengeService.endChallenge(challengeId, userDetails.getId());
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

    @GetMapping("/getCurrentQuestion")
    public ResponseEntity<PublishQuestionResponse> getCurrentQuestion(@RequestParam int challengeId) throws Exception {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostOrganizeChallengeService.getCurrentQuestion(challengeId, userDetails.getId()));
    }


}
