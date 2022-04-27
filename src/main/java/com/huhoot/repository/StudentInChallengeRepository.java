package com.huhoot.repository;

import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.model.StudentInChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentInChallengeRepository extends JpaRepository<StudentInChallenge, Integer> {

    Page<StudentInChallenge> findAllByPrimaryKeyStudentUsernameContainingIgnoreCaseAndPrimaryKeyChallengeId(String studentUsername, int challengeId, Pageable pageable);

    List<StudentInChallenge> findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(int challengeId, int adminId);

    /**
     * @param studentIds  List of {@link com.huhoot.model.Student} ids
     * @param challengeId {@link com.huhoot.model.Challenge} id
     * @param adminId     {@link com.huhoot.model.Admin} id
     * @return List of {@link StudentInChallenge}
     */
    @Query("SELECT n " +
            "FROM StudentInChallenge n " +
            "WHERE n.primaryKey.student.id IN :studentIds " +
            "AND n.primaryKey.challenge.id = :challengeId " +
            "AND n.primaryKey.challenge.admin.id = :adminId")
    List<StudentInChallenge> findAllByStudentIdInAndChallengeIdAndAdminId(@Param("studentIds") List<Integer> studentIds,
                                                                          @Param("challengeId") int challengeId,
                                                                          @Param("adminId") int adminId);

    Optional<StudentInChallenge> findOneByPrimaryKeyStudentIdAndPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(int studentId, int challengeId, int adminId);

    @Query("SELECT n " +
            "FROM StudentInChallenge n " +
            "WHERE n.primaryKey.challenge.id = :challengeId " +
            "AND n.primaryKey.student.id = :studentId " +
            "AND n.isNonDeleted = TRUE " +
            "AND n.isKicked = FALSE " +
            "AND n.primaryKey.challenge.challengeStatus NOT IN (com.huhoot.enums.ChallengeStatus.BUILDING, com.huhoot.enums.ChallengeStatus.ENDED)")
    Optional<StudentInChallenge> findOneByChallengeIdAndStudentIdAndAvailable(@Param("challengeId") int challengeId, @Param("studentId") int studentId);


    @Query("SELECT new com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse(n.primaryKey.student.id, n.primaryKey.student.username, n.primaryKey.student.fullName, n.isLogin, n.isKicked, n.isOnline, n.createdBy, n.createdDate, n.modifiedBy, n.modifiedDate, n.isNonDeleted) " +
            "FROM StudentInChallenge n " +
            "WHERE n.primaryKey.challenge.id = :challengeId " +
            "AND n.primaryKey.challenge.admin.id = :adminId " +
            "AND n.isLogin = TRUE " +
            "AND n.isNonDeleted = TRUE " +
            "AND n.isKicked = FALSE")
    List<StudentInChallengeResponse> findAllStudentIsLogin(@Param("challengeId") int challengeId, @Param("adminId") int adminId);

    @Query("SELECT new com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse(n.primaryKey.student.id, n.primaryKey.student.username, n.primaryKey.student.fullName, n.isLogin, n.isKicked, n.isOnline, n.createdBy, n.createdDate, n.modifiedBy, n.modifiedDate,  n.isNonDeleted) " +
            "FROM StudentInChallenge n " +
            "WHERE n.primaryKey.challenge.id = :challengeId and n.primaryKey.challenge.admin.id = :adminId")
    Page<StudentInChallengeResponse> findAllByChallengeIdAndAdminId(@Param("challengeId") int challengeId, @Param("adminId") int adminId, Pageable pageable);

    @Query("SELECT COUNT(a.primaryKey.student.id) FROM StudentInChallenge a WHERE a.primaryKey.challenge.id = :challengeId AND a.isLogin = true")
    int getTotalStudentInChallenge(@Param("challengeId") int challengeId);
}
