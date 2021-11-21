package com.huhoot.repository;

import com.huhoot.model.StudentInChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentChallengeRepository extends JpaRepository<StudentInChallenge, Integer> {

    Page<StudentInChallenge> findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminIdAndIsNonDeletedFalse(int challengeId, int id, Pageable pageable);

    List<StudentInChallenge> findAllByPrimaryKeyStudentIdInAndPrimaryKeyChallengeId(List<Integer> studentIds, int challengeId);

    Page<StudentInChallenge> findAllByPrimaryKeyStudentUsernameContainingIgnoreCaseAndPrimaryKeyChallengeId(String studentUsername, int challengeId, Pageable pageable);

    List<StudentInChallenge> findAllByPrimaryKeyStudentIdAndIsNonDeletedFalse(int id, Pageable pageable);


    List<StudentInChallenge> findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminIdAndIsNonDeletedFalse(int challengeId, int adminId);
}
