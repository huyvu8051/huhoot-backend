package com.huhoot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.huhoot.model.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer>{
	Student findOneByUsername(String username);

    Page<Student> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Optional<Student> findOneById(int id);

    Page<Student> findAllByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
