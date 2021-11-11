package com.huhoot.config;

import com.huhoot.enums.Role;
import com.huhoot.model.Admin;
import com.huhoot.model.Student;
import com.huhoot.repository.AdminRepository;
import com.huhoot.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    public void run(ApplicationArguments args) throws IOException {

        Admin admin = new Admin("admin", passwordEncoder.encode("admin"));
        admin.setRole(Role.ADMIN);

        adminRepository.save(admin);

        for (int i = 0; i < 10; i++) {
            adminRepository.save(new Admin("admin" + i, passwordEncoder.encode("admin")));

            studentRepository.save(new Student("student" + i, "student" + i, passwordEncoder.encode("student")));
        }

        // start time
        long t0 = System.nanoTime();

        long t1 = System.nanoTime();

        // end time

        double elapsedTimeInSecond = (double) (t1 - t0) / 1_000_000_000;
        System.out.println("Elapsed time =" + elapsedTimeInSecond + " seconds");

        File tempDirectory = new File(new File(System.getProperty("java.io.tmpdir")), "files");
        if (tempDirectory.exists()) {
            System.out.println("something");
        } else {
            tempDirectory.mkdirs();
        }

        File file = new File(tempDirectory.getAbsolutePath() + "/abcd.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        String file2 = new File(tempDirectory.getAbsolutePath() + "/something.txt").getAbsolutePath();


        System.out.println(file2);

    }
}