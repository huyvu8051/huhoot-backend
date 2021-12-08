package com.huhoot.repository;

import com.huhoot.dto.PublishQuestion;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.model.Challenge;
import com.huhoot.model.Question;
import com.huhoot.model.StudentInChallenge;
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

    Optional<Challenge> findOneById(int id);

    Page<Challenge> findAllByTitleContainingIgnoreCaseAndAdminId(String title, int id, Pageable pageable);

    Page<Challenge> findAllByAdminId(int id, Pageable pageable);

    Page<Challenge> findAll(Pageable pageable);

    Page<Challenge> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Challenge> findAllByAdminIdAndIdIn(int id, List<Integer> ids);

    /**
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link com.huhoot.model.Admin} id
     * @return Challenge
     */
    Optional<Challenge> findOneByIdAndAdminId( int challengeId, int adminId);

    /**
     * Update challenge status
     *
     * @param challengeStatus challenge status
     * @param challengeId     challenge id
     * @param adminId         admin id
     */
    @Modifying
    @Transactional
    @Query("UPDATE Challenge c " +
            "SET c.challengeStatus = :challengeStatus " +
            "WHERE c.id =:challengeId AND c.admin.id = :adminId ")
    void updateChallengeStatusByIdAndAdminId(@Param("challengeStatus") ChallengeStatus challengeStatus,
                                             @Param("challengeId") int challengeId,
                                             @Param("adminId") int adminId);


    /**
     * @param questionId question id
     * @param adminId    admin id
     * @return challenge id
     */
    @Query("SELECT n.challenge.id " +
            "FROM Question n " +
            "WHERE n.id = :questionId AND n.challenge.admin.id = :adminId")
    Optional<Integer> findOneByQuestionIdAndAdminId(@Param("questionId") int questionId,@Param("adminId") int adminId);

    @Query("SELECT n.questions.size " +
            "FROM Challenge n " +
            "WHERE  n.id = :challengeId")
    int findCountQuestion(@Param("challengeId") int challengeId);

    @Modifying
    @Transactional
    @Query("UPDATE Challenge n " +
            "SET n.currentQuestionId = :questionId " +
            "WHERE n.id = :challengeId")
    void updateCurrentQuestionId(@Param("challengeId")int challengeId,
                                 @Param("questionId") int currentQuestionId);


    @Query("SELECT n.currentQuestionId " +
            "FROM Challenge n " +
            "WHERE n.id = :challengeId AND n.admin.id = :adminId")
    Optional<Integer> findCurrentQuestionId(@Param("challengeId") int challengeId,
                                            @Param("adminId") int adminId);


    @Query("SELECT new com.huhoot.dto.PublishQuestion(m.id, m.ordinalNumber, m.questionContent, m.questionImage, m.answerTimeLimit, m.point, m.askDate, m.answerOption, m.challenge.id, m.challenge.questions.size) " +
            "FROM Question m " +
            "WHERE m.id IN (SELECT n.currentQuestionId FROM Challenge n WHERE n.id = :challengeId AND n.admin.id = :adminId)")
    Optional<PublishQuestion> findCurrentPublishedQuestion(@Param("challengeId") int challengeId,
                                                           @Param("adminId") int adminId);


    @Query("SELECT n.primaryKey.challenge " +
            "FROM StudentInChallenge n " +
            "WHERE n.primaryKey.student.id = :studentId AND n.primaryKey.challenge.isNonDeleted = TRUE AND n.primaryKey.challenge.challengeStatus <> com.huhoot.enums.ChallengeStatus.BUILDING")
    List<Challenge> findAllByStudentIdAndIsAvailable(@Param("studentId") int studentId, Pageable pageable);
}
