package com.huhoot.repository;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.huhoot.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findOneByUsername(String username);

    Optional<Student> findOneById(int id);

    Page<Student> findAllByIsNonLocked(boolean isLocked, Pageable pageable);

    Page<Student> findAllByUsernameContainingIgnoreCaseAndIsNonLocked(String username, boolean isNonLocked, Pageable pageable);

    @Query("SELECT n.primaryKey.student FROM StudentInChallenge n WHERE n.primaryKey.challenge.id = :challengeId")
    List<Student> findAllStudentInChallenge(@Param("challengeId") int challengeId);

    List<Student> findAllByIdIn(List<Integer> studentIds);

    @Modifying
    @Transactional
    @Query("UPDATE Student n " +
            "SET n.socketId = :socketId " +
            "WHERE n.id = :studentId")
    void updateSocketId(@Param("socketId") UUID socketId, @Param("studentId") int studentId);
}
