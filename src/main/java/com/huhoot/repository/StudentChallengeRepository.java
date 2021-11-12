package com.huhoot.repository;

import com.huhoot.model.Student;
import com.huhoot.model.StudentChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentChallengeRepository extends JpaRepository<StudentChallenge, Integer> {
}
