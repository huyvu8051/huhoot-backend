package com.huhoot.autoorganize;

import com.huhoot.model.Student;
import com.huhoot.organize.OrganizeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController("autoOrganizeController")
@RequestMapping("autoOrganize")
public class AutoOrganizeController {

    private final OrganizeService organizeService;

    @GetMapping("/showCorrectAnswer")
    public void showCorrectAnswer(@RequestParam int questionId) throws NullPointerException {
        Student student = (Student) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

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

}
