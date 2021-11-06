package com.huhoot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.huhoot.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>{
	Student findOneByUsername(String username);

    Page<Student> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Student findOneById(int id);

    Page<Student> findAllByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
