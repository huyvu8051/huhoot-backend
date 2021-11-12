package com.huhoot.config;

import com.huhoot.enums.AnswerTime;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.enums.Points;
import com.huhoot.enums.Role;
import com.huhoot.exception.AnswerOption;
import com.huhoot.model.*;
import com.huhoot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentChallengeRepository studentChallengeRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public void run(ApplicationArguments args) throws IOException {
        Date date = new Date();

        // start time
        long t0 = System.nanoTime();

        Admin admin = new Admin("admin", passwordEncoder.encode("admin"));
        admin.setRole(Role.ADMIN);
        admin.setCreatedDate(date);
        admin.setCreatedBy("BobVu");
        admin.setModifiedDate(date);
        admin.setModifiedBy("Nobody");

        adminRepository.save(admin);

        for (int i = 0; i < 3; i++) {
            Admin admin1 = new Admin("admin" + i, passwordEncoder.encode("admin"));

            admin1.setRole(Role.ADMIN);
            admin1.setCreatedDate(date);
            admin1.setCreatedBy("BobVu");
            admin1.setModifiedDate(date);
            admin1.setModifiedBy("Nobody");

            Admin host = adminRepository.save(admin1);


            for (int j = 0; j < 3; j++) {
                Challenge challenge = new Challenge();
                challenge.setTitle("title " + i + j);
                challenge.setChallengeStatus(ChallengeStatus.WAITING);
                challenge.setCoverImage("cover image" + i + j);
                challenge.setRandomQuest(true);
                challenge.setRandomAnswer(true);
                challenge.setAdmin(host);
                challenge.setCreatedDate(date);
                challenge.setCreatedBy("BobVu");
                challenge.setModifiedDate(date);
                challenge.setModifiedBy("Nobody");

                Challenge chall = challengeRepository.save(challenge);


                for (int x = 0; x < 7; x++) {
                    Student student1 = new Student("student" + i + j, "student" + i + j, passwordEncoder.encode("student"));
                    student1.setCreatedDate(date);
                    student1.setCreatedBy("BobVu");
                    student1.setModifiedDate(date);
                    student1.setModifiedBy("Nobody");
                    Student student = studentRepository.save(student1);

                    StudentChallenge studentChallenge = new StudentChallenge();
                    studentChallenge.setStudent(student);
                    studentChallenge.setChallenge(chall);
                    studentChallenge.setCreatedDate(date);
                    studentChallenge.setCreatedBy("BobVu");
                    studentChallenge.setModifiedDate(date);
                    studentChallenge.setModifiedBy("Nobody");

                    studentChallengeRepository.save(studentChallenge);

                    Question question
                            = new Question();
                    question.setOrdinalNumber(x);
                    question.setQuestionContent("content " + i + j + x);
                    question.setAnswerTimeLimit(AnswerTime.TWENTY_SEC);
                    question.setPoint(Points.STANDARD);
                    question.setAnswerOption(AnswerOption.SINGLE_SELECT);

                    question.setChallenge(chall);

                    question.setCreatedDate(date);
                    question.setCreatedBy("BobVu");
                    question.setModifiedDate(date);
                    question.setModifiedBy("Nobody");

                    Question quest = questionRepository.save(question);

                    for (int a = 0; a < 4; a++) {
                        Answer answer = new Answer();
                        answer.setOrdinalNumber(a);
                        answer.setAnswerContent("content " + i + j + x + a);
                        answer.setCorrect((i + j + x + a) % 2 == 0);
                        answer.setQuestion(quest);

                        answer.setCreatedDate(date);
                        answer.setCreatedBy("BobVu");
                        answer.setModifiedDate(date);
                        answer.setModifiedBy("Nobody");

                        answerRepository.save(answer);
                    }

                }

            }
        }


        long t1 = System.nanoTime();

/*

        List<Answer> answers = new ArrayList<>();
        for(int i = 0; i < 100000; i++){
            Answer answer = new Answer();
            answer.setOrdinalNumber(i);
            answer.setAnswerContent("content " + i);
            answer.setCorrect((i) % 2 == 0);

            answer.setCreatedDate(date);
            answer.setCreatedBy("BobVu");
            answer.setModifiedDate(date);
            answer.setModifiedBy("Nobody");

            answers.add(answer);
        }

        answerRepository.saveAll(answers); */

        // end time

        double elapsedTimeInSecond = (double) (t1 - t0) / 1_000_000_000;
        System.out.println("Elapsed time =" + elapsedTimeInSecond + " seconds");

    }
}