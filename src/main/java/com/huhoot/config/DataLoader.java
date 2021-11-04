package com.huhoot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.huhoot.model.Admin;
import com.huhoot.model.Role;
import com.huhoot.model.Student;
import com.huhoot.repository.IAdminRepository;
import com.huhoot.repository.IStudentRepository;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IStudentRepository iStudentRepository;

	@Autowired
	private IAdminRepository iAdminRepository;

	public void run(ApplicationArguments args) {

		Admin admin = new Admin("admin", passwordEncoder.encode("admin"));
		admin.setRole(Role.ADMIN);

		iAdminRepository.save(admin);

		for (int i = 0; i < 10; i++) {
			iAdminRepository.save(new Admin("admin" + i, passwordEncoder.encode("admin")));

			iStudentRepository.save(new Student("student" + i, "student" + i, passwordEncoder.encode("student")));
		}

		// start time
		long t0 = System.nanoTime();

		long t1 = System.nanoTime();

		// end time

		double elapsedTimeInSecond = (double) (t1 - t0) / 1_000_000_000;
		System.out.println("Elapsed time =" + elapsedTimeInSecond + " seconds");

	}
}