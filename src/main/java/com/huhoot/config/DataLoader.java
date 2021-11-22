package com.huhoot.config;

import com.huhoot.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;

    private final AdminRepository adminRepository;

    private final ChallengeRepository challengeRepository;

    private final QuestionRepository questionRepository;

    private final StudentInChallengeRepository studentChallengeRepository;

    private final AnswerRepository answerRepository;

    public DataLoader(PasswordEncoder passwordEncoder, StudentRepository studentRepository, AdminRepository adminRepository, ChallengeRepository challengeRepository, QuestionRepository questionRepository, StudentInChallengeRepository studentChallengeRepository, AnswerRepository answerRepository) {
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.challengeRepository = challengeRepository;
        this.questionRepository = questionRepository;
        this.studentChallengeRepository = studentChallengeRepository;
        this.answerRepository = answerRepository;
    }

    public void run(ApplicationArguments args) throws IOException {



        Date date = new Date();

        // start time
        long t0 = System.nanoTime();
/*
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

                challenge.setCoverImage(getRandomImgUrl());
                challenge.setRandomQuest(true);
                challenge.setRandomAnswer(true);
                challenge.setAdmin(host);
                challenge.setCreatedDate(date);
                challenge.setCreatedBy("BobVu");
                challenge.setModifiedDate(date);
                challenge.setModifiedBy("Nobody");

                Challenge chall = challengeRepository.save(challenge);


                for (int x = 0; x < 7; x++) {
                    Student student1 = new Student("student" + i + j + x, "student" + i + j, passwordEncoder.encode("student"));
                    student1.setCreatedDate(date);
                    student1.setCreatedBy("BobVu");
                    student1.setModifiedDate(date);
                    student1.setModifiedBy("Nobody");
                    Student student = studentRepository.save(student1);

                    StudentInChallenge studentChallenge = new StudentInChallenge();
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
                    question.setQuestionContent(getRandomImgUrl());
                    question.setAnswerTimeLimit(AnswerTime.TWENTY_SEC);
                    question.setPoint(Points.STANDARD);
                    question.setAnswerOption(AnswerOption.SINGLE_SELECT);

                    question.setChallenge(chall);



                    question.setCreatedDate(date);
                    question.setCreatedBy("BobVu");
                    question.setModifiedDate(date);
                    question.setModifiedBy("Nobody");

                    Question quest = questionRepository.save(question);

                    List<Answer> answers = new ArrayList<>();

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

                        answers.add(answer);
                    }
                    answerRepository.saveAll(answers);

                }

            }
        }


        long t1 = System.nanoTime();

/*

        List<Answer> answers = new ArrayList<>();
        for(int i = 0; i < 10; i++){
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

        answerRepository.saveAll(answers);



        // end time

        double elapsedTimeInSecond = (double) (t1 - t0) / 1_000_000_000;
        System.out.println("Elapsed time =" + elapsedTimeInSecond + " seconds");


 */
    }

    private String getRandomImgUrl(){
        List<String> imgUrls = new ArrayList<>();

        imgUrls.add("https://i.imgur.com/BpnU5U9.jpeg");
        imgUrls.add("https://i.imgur.com/ZMovdZu.jpg");
        imgUrls.add("https://i.imgur.com/e03sCWa.jpg");
        imgUrls.add("https://i.imgur.com/nI5UWV8.jpg");
        imgUrls.add("https://i.imgur.com/o9CI562.jpg");
        imgUrls.add("https://i.imgur.com/9bjVfKI.jpg");

        Random random = new Random();
        int rand = random.nextInt(6);

        return imgUrls.get(rand);
    }
}