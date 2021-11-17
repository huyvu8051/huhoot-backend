package com.huhoot.repository;

import com.huhoot.model.Student;
import com.huhoot.model.StudentChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentChallengeRepository extends JpaRepository<StudentChallenge, Integer> {

    Page<StudentChallenge> findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminIdAndIsDeletedFalse(int challengeId, int id, Pageable pageable);

    Optional<StudentChallenge> findOneByPrimaryKeyStudentIdAndPrimaryKeyChallengeId(int id, int id1);

    List<StudentChallenge> findAllByPrimaryKeyStudentIdInAndPrimaryKeyChallengeId(List<Integer> studentIds, int challengeId);

    Page<StudentChallenge> findAllByPrimaryKeyStudentUsernameContainingIgnoreCaseAndPrimaryKeyChallengeId(String studentUsername, int challengeId, Pageable pageable);
}
