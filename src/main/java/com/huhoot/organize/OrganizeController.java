package com.huhoot.organize;

import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.model.Admin;
import com.huhoot.model.Question;
import com.huhoot.repository.QuestionRepository;
import com.huhoot.vue.vdatatable.paging.PageResponse;
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


    private final QuestionRepository questionRepository;
    private final OrganizeService organizeService;

    @GetMapping("/openChallenge")
    public ResponseEntity<List<StudentInChallengeResponse>> openChallenge(@RequestParam int challengeId) throws Exception {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(organizeService.openChallenge(userDetails, challengeId));
    }

    @GetMapping("/studentOnline")
    public ResponseEntity<List<StudentInChallengeResponse>> updateStudentOnline(@RequestParam int challengeId) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(organizeService.getAllStudentInChallengeIsLogin(userDetails, challengeId));
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

        organizeService.startChallenge(challengeId, userDetails.getId());
    }


    /**
     * Show correct answer
     *
     * @param questionId {@link com.huhoot.model.Question} id
     * @throws NullPointerException not found
     */
    @GetMapping("/showCorrectAnswer")
    public void showCorrectAnswer(@RequestParam int questionId) throws NullPointerException {
        Question question = questionRepository.findOneByIdAndAskDateNotNull(questionId).orElseThrow(() -> new NullPointerException("Question not found"));
        organizeService.showCorrectAnswer(question);
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

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        return ResponseEntity.ok(organizeService.getTopStudent(challengeId, pageable));
    }


    /**
     * @param challengeId {@link com.huhoot.model.Challenge} id
     * @return List of {@link StudentScoreResponse}
     * @throws NullPointerException not found
     */
    @GetMapping("/endChallenge")
    public void endChallenge(@RequestParam int challengeId) throws NullPointerException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        organizeService.endChallenge(challengeId);
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
        organizeService.kickStudent(req.getStudentIds(), req.getChallengeId(), userDetails.getId());
    }

    @GetMapping("/publishNextQuestion")
    public void publishNextQuestion(@RequestParam int challengeId) throws Exception {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        try {
            organizeService.publishNextQuestion(challengeId);
        } catch (Exception e) {
            organizeService.endChallenge(challengeId);
        }
    }





}
