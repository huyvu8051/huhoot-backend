package com.huhoot.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.github.javafaker.Faker;
import com.huhoot.encrypt.EncryptUtils;
import com.huhoot.repository.StudentRepository;
import com.huhoot.enums.AnswerOption;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.enums.Points;
import com.huhoot.enums.Role;
import com.huhoot.model.*;
import com.huhoot.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@AllArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final ChallengeRepository challengeRepository;
    private final QuestionRepository questionRepository;
    private final StudentInChallengeRepository studentChallengeRepository;
    private final AnswerRepository answerRepository;
    private final EncryptUtils encryptUtils;



    @Autowired
    private SocketIOServer server;


    public void run(ApplicationArguments args) {

        try {
            server.start();
            log.info("Socket launch successful!");
        } catch (Exception e) {
            log.error("Socket launch failure!");
            log.error(e.getMessage());
        }

        Random random = new Random();

        Faker faker = new Faker(new Locale("vi-VN"));


        long date = System.currentTimeMillis();

        // start time
        long t0 = System.nanoTime();

        Optional<Admin> op = adminRepository.findOneByUsername("admin");

        if (!op.isPresent()) {


            /*Admin adm = Admin.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ADMIN)
                    .createdDate(date)
                    .createdBy("BobVu")
                    .modifiedDate(date)
                    .modifiedBy("BobVu")
                    .build();

            adminRepository.save(adm);*/


            Admin admin = new Admin("admin", passwordEncoder.encode("password"));
            admin.setRole(Role.ADMIN);
            admin.setCreatedDate(date);
            admin.setCreatedBy("System");
            admin.setModifiedDate(date);
            admin.setModifiedBy("System");

            adminRepository.save(admin);

            for (int i = 0; i < 1; i++) {
                Admin admin1 = new Admin("admin" + i, passwordEncoder.encode("password"));

                admin1.setRole(Role.HOST);
                admin1.setCreatedDate(date);
                admin1.setCreatedBy("BobVu");
                admin1.setModifiedDate(date);
                admin1.setModifiedBy("Nobody");

                Admin host = adminRepository.save(admin1);


                for (int j = 0; j < 1; j++) {
                    Challenge challenge = new Challenge();



                    challenge.setTitle(faker.lorem().paragraph());
                    challenge.setChallengeStatus(ChallengeStatus.WAITING);

                    challenge.setCoverImage(faker.internet().image());
                    challenge.setRandomQuest(true);
                    challenge.setRandomAnswer(true);
                    challenge.setAdmin(host);
                    challenge.setCreatedDate(date);
                    challenge.setCreatedBy("BobVu");
                    challenge.setModifiedDate(date);
                    challenge.setModifiedBy("Nobody");

                    Challenge chall = challengeRepository.save(challenge);


                    for (int x = 0; x < 7; x++) {
                        Student student1 = new Student("student" + i + j + x,  faker.name().fullName(), passwordEncoder.encode("password"));
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
                        studentChallenge.setNonDeleted(true);

                        studentChallengeRepository.save(studentChallenge);


                        int answerTime = 10 + random.nextInt(25);

                        Question question
                                = new Question();
                        question.setOrdinalNumber(x);
                        question.setQuestionContent(faker.lorem().paragraph());
                        question.setQuestionImage(getRandomImgUrl());
                        question.setAnswerTimeLimit(answerTime);
                        question.setPoint(Points.STANDARD);
                        question.setAnswerOption(AnswerOption.MULTI_SELECT);

                        question.setChallenge(chall);

                        byte[] key = encryptUtils.generateRandomKey().getEncoded();
                        question.setEncryptKey(key);

                        question.setCreatedDate(date);
                        question.setCreatedBy("BobVu");
                        question.setModifiedDate(date);
                        question.setModifiedBy("Nobody");

                        Question quest = questionRepository.save(question);

                        List<Answer> answers = new ArrayList<>();

                        int i1 = random.nextInt(6);

                        for (int a = 0; a < i1 + 1; a++) {
                            int randAnswerCorrect = random.nextInt(10);
                            boolean corr = (randAnswerCorrect % 3) == 0;
                            Answer answer = new Answer();
                            answer.setOrdinalNumber(a);


                            answer.setAnswerContent(faker.lorem().paragraph() + " " + corr);
                            answer.setCorrect(corr);
                            answer.setQuestion(quest);

                            answer.setCreatedDate(date);
                            answer.setCreatedBy("BobVu");
                            answer.setModifiedDate(date);
                            answer.setModifiedBy("Nobody");

                            answers.add(answer);
                        }

                        if(answers.stream().noneMatch(e->e.isCorrect())) answers.get(0).setCorrect(true);

                        answerRepository.saveAll(answers);

                    }

                }
            }


        }
    }

    private String getRandomImgUrl() {
        List<String> imgUrls = new ArrayList<>();

        imgUrls.add("1da20102-db0f-4820-bb19-ed7d07e54cdb.jpg");
        imgUrls.add("5d2500ce-8c22-4b43-a3c9-842bb866204a.jpg");
        imgUrls.add("7b6f4ba8-347e-4452-b66f-1a00b99b5c8a.jpg");
        imgUrls.add("26e49ab7-0752-44b8-9aba-915937064e00.jpg");
        imgUrls.add("285a4e82-2ad4-462a-bfe3-e9b9ab846e7f.png");
        imgUrls.add("9795ce36-e07b-438a-ba88-428d11588398.jpg");
        imgUrls.add("68820dbd-b439-45cc-bb2a-5a50a758b725.jpg");
        imgUrls.add("a22a4558-039e-4522-bb15-af863e95eab1.jpg");
        imgUrls.add("ab804242-d08e-4f9d-9ec0-42c9082e945f.jpeg");
        imgUrls.add("b9759822-f86a-4fae-a06d-aaef15a162a0.jpg");
        imgUrls.add("ce84e707-af4f-40d1-920f-e17d26a39ce7.png");
        imgUrls.add("f57f24e9-69d2-4d26-9ac7-e05ee9dcdae3.svg");

        Random random = new Random();
        int rand = random.nextInt(6);

        return imgUrls.get(rand);
    }

}