package com.huhoot.repository;

import com.huhoot.enums.ChallengeStatus;
import com.huhoot.host.manage.challenge.ChallengeResponse;
import com.huhoot.organize.PublishQuestion;
import com.huhoot.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {


    @Query("SELECT new com.huhoot.host.manage.challenge.ChallengeResponse(n.id, n.title, n.coverImage, n.randomAnswer, " +
            "n.randomQuest, n.challengeStatus, n.admin.username, n.admin.socketId, n.createdDate, n.createdBy, " +
            "n.modifiedDate, n.modifiedBy) " +
            "FROM Challenge n " +
            "WHERE n.admin.id = :adminId")
    Page<ChallengeResponse> findAllByAdminId(@Param("adminId") int adminId, Pageable pageable);

    Page<Challenge> findAll(Pageable pageable);


    /**
     * @param challengeId {@link Challenge} id
     * @return Challenge
     */
    Optional<Challenge> findOneById(int challengeId);

    /**
     * Update challenge status
     *
     * @param challengeStatus challenge status
     * @param challengeId     challenge id
     */
    @Modifying
    @Transactional
    @Query("UPDATE Challenge c " +
            "SET c.challengeStatus = :challengeStatus " +
            "WHERE c.id =:challengeId")
    void updateChallengeStatusById(@Param("challengeStatus") ChallengeStatus challengeStatus,
                                   @Param("challengeId") int challengeId);


    /**
     * @param questionId question id
     * @return challenge id
     */
    @Query("SELECT n.challenge " +
            "FROM Question n " +
            "WHERE n.id = :questionId")
    Optional<Challenge> findOneByQuestionId(@Param("questionId") int questionId);








    @Query("SELECT n.primaryKey.challenge " +
            "FROM StudentInChallenge n " +
            "WHERE n.primaryKey.student.id = :studentId AND n.primaryKey.challenge.isNonDeleted = TRUE " +
            "AND n.primaryKey.challenge.challengeStatus <> com.huhoot.enums.ChallengeStatus.BUILDING")
    List<Challenge> findAllByStudentIdAndIsAvailable(@Param("studentId") int studentId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Challenge n " +
            "SET n.studentOrganizeId = :studentOrganizeId " +
            "WHERE n.id = :challengeId")
    void updateStudentOrganizeId(@Param("challengeId")int challengeId,
                                 @Param("studentOrganizeId") String studentOrganizeId);
}
