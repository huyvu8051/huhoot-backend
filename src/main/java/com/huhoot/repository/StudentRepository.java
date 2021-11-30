package com.huhoot.repository;

import com.huhoot.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer>{
	Student findOneByUsername(String username);

    Optional<Student> findOneById(int id);

    Page<Student> findAllByIsNonLocked(boolean isLocked, Pageable pageable);

    Page<Student> findAllByUsernameContainingIgnoreCaseAndIsNonLocked(String username, boolean isNonLocked, Pageable pageable);

    List<Student> findAllByStudentChallengesPrimaryKeyChallengeId(int id);

    List<Student> findAllByIdIn(List<Integer> studentIds);
}
