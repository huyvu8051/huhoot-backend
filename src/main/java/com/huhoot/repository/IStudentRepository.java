package com.huhoot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huhoot.model.Student;

public interface IStudentRepository extends JpaRepository<Student, Integer>{
	Student findOneByUsername(String username);
}
