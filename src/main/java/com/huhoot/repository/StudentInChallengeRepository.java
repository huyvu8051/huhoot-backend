package com.huhoot.repository;

import com.huhoot.model.StudentInChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentInChallengeRepository extends JpaRepository<StudentInChallenge, Integer> {

    Page<StudentInChallenge> findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(int challengeId, int id, Pageable pageable);

    List<StudentInChallenge> findAllByPrimaryKeyStudentIdInAndPrimaryKeyChallengeId(List<Integer> studentIds, int challengeId);

    Page<StudentInChallenge> findAllByPrimaryKeyStudentUsernameContainingIgnoreCaseAndPrimaryKeyChallengeId(String studentUsername, int challengeId, Pageable pageable);

    List<StudentInChallenge> findAllByPrimaryKeyStudentIdAndIsNonDeletedTrue(int id, Pageable pageable);


    List<StudentInChallenge> findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(int challengeId, int adminId);

    Optional<StudentInChallenge> findOneByPrimaryKeyChallengeIdAndPrimaryKeyStudentId(int challengeId, int id);

    List<StudentInChallenge> findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminIdAndIsLoginTrue(int challengeId, int id);

    /**
     * @param studentIds  List of {@link com.huhoot.model.Student} ids
     * @param challengeId {@link com.huhoot.model.Challenge} id
     * @param adminId     {@link com.huhoot.model.Admin} id
     * @return List of {@link StudentInChallenge}
     */
    @Query("SELECT n " +
            "FROM StudentInChallenge n " +
            "WHERE n.primaryKey.student.id IN :studentIds AND n.primaryKey.challenge.id = :challengeId AND n.primaryKey.challenge.admin.id = :adminId")
    List<StudentInChallenge> findAllByStudentIdInAndChallengeIdAndChallengeAdminId(List<Integer> studentIds, int challengeId, int adminId);

    Optional<StudentInChallenge> findOneByPrimaryKeyStudentIdAndPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(int studentId, int challengeId, int adminId);
}
