package com.huhoot.autoorganize;

import com.huhoot.model.Challenge;
import com.huhoot.model.Student;
import com.huhoot.organize.OrganizeService;
import com.huhoot.repository.ChallengeRepository;
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


    @GetMapping("/showCorrectAnswer")
    public void showCorrectAnswer(@RequestParam int questionId) throws NullPointerException {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
//                .getPrincipal();
//
//        String username = userDetails.getUsername();
//
//        Challenge challenge = challengeRepository.findOneByQuestionId(questionId).orElseThrow(() -> new NullPointerException("challenge not found"));
//
//        if(challenge.getStudentOrganizeId() != username){
//            throw new NullPointerException("id not match");
//        }

        organizeService.showCorrectAnswer(questionId);
    }

    @GetMapping("/publishNextQuestion")
    public void publishNextQuestion(@RequestParam int challengeId) throws Exception {

        try {
            organizeService.publishNextQuestion(challengeId);
        } catch (Exception e) {
            organizeService.endChallenge(challengeId);
        }
    }

    @GetMapping("/enableAutoOrganize")
    public void enableAutoOrganize(@RequestParam int challengeId) throws Exception {
        organizeService.findAnyClientAndEnableAutoOrganize(challengeId);
    } @GetMapping("/disableAutoOrganize")
    public void disableAutoOrganize(@RequestParam int challengeId) throws Exception {
        organizeService.disableAutoOrganize(challengeId);
    }

}
