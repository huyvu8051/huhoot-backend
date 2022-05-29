package com.huhoot.autoorganize;

import com.huhoot.exception.NoClientInBroadcastOperations;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.model.CustomUserDetails;
import com.huhoot.model.Question;
import com.huhoot.organize.OrganizeService;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController("autoOrganizeController")
@RequestMapping("autoOrganize")
public class AutoOrganizeController {

    private final OrganizeService organizeService;
    private final ChallengeRepository challengeRepository;
    private final QuestionRepository questionRepository;

    @GetMapping("/showCorrectAnswer")
    public void showCorrectAnswer(@RequestParam int questionId) throws NullPointerException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();


        Question question = questionRepository.findOneByIdAndAskDateNotNull(questionId).orElseThrow(() -> new NullPointerException("Question not found"));

        Challenge chall = challengeRepository.findOneByIdByAutoOrganizer(question.getChallenge().getId(), userDetails.getId()).orElseThrow(() -> new NullPointerException("Challenge not found"));

        organizeService.showCorrectAnswer(question);
    }

    @GetMapping("/publishNextQuestion")
    public void publishNextQuestion(@RequestParam int challengeId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Challenge chall = challengeRepository.findOneByIdByAutoOrganizer(challengeId, userDetails.getId()).orElseThrow(() -> new NullPointerException("Challenge not found"));

        try {
            organizeService.publishNextQuestion(challengeId);
        } catch (Exception e) {
            organizeService.endChallenge(challengeId);
        }
    }

    @GetMapping("/enableAutoOrganize")
    public void enableAutoOrganize(@RequestParam int challengeId) {

        try {
            organizeService.findAnyClientAndEnableAutoOrganize(challengeId);
        } catch (NoClientInBroadcastOperations e) {
            organizeService.disableAutoOrganize(challengeId);
            organizeService.updateChallengeStatusToClient(challengeId);
        }
    }

    @GetMapping("/disableAutoOrganize")
    public void disableAutoOrganize(@RequestParam int challengeId) {
        organizeService.disableAutoOrganize(challengeId);
        organizeService.updateChallengeStatusToClient(challengeId);
    }

}
