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
import java.nio.file.Path;
import java.nio.file.Paths;

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

        Path uploadDir = Paths.get("/");
        System.out.println(uploadDir.toAbsolutePath());

    }
}