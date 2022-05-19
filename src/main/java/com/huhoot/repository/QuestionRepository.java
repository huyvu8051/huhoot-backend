package com.huhoot.repository;

import com.huhoot.host.manage.question.QuestionResponse;
import com.huhoot.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Optional<Question> findOneById(int id);


    List<Question> findAllByChallengeId(int id);

    Optional<Question> findOneByIdAndAskDateNotNull(int questionId);



    /**
     * @param askDate    ask date
     * @param questionId question id
     */
    @Transactional
    @Modifying
    @Query("UPDATE Question q " +
            "SET q.askDate = :askDate, " +
            "q.publishedOrderNumber = :publishedOrderNumber " +
            "WHERE q.id = :questionId")
    void updateAskDateAndPublishedOrderNumber(@Param("askDate") long askDate,
                                              @Param("publishedOrderNumber") Integer publishedOrderNumber,
                                              @Param("questionId") int questionId);



    Optional<Question> findFirstByChallengeIdAndAskDateNullOrderByOrdinalNumberAsc(int challengeId);

    @Query("SELECT COUNT(n.id) FROM Question n WHERE n.challenge.id = :challengeId AND n.askDate IS NOT NULL")
    int findNumberOfPublishedQuestion(@Param("challengeId") int challengeId);

    @Query("SELECT new com.huhoot.host.manage.question.QuestionResponse(n.id, n.ordinalNumber, n.questionContent, " +
            "n.questionImage, n.answerTimeLimit, n.point, n.answerOption, n.askDate, n.isNonDeleted, n.createdDate, " +
            "n.createdBy, n.modifiedDate, n.modifiedBy) " +
            "FROM Question n " +
            "WHERE n.challenge.id = :challengeId " +
            "AND n.challenge.admin.id = :adminId " +
            "ORDER BY n.ordinalNumber ASC")
    Page<QuestionResponse> findAllByChallengeIdAndAdminId(@Param("challengeId") int challengeId, @Param("adminId") int adminId, Pageable pageable);

    @Query("SELECT COALESCE(MAX (n.ordinalNumber + 1), 0) " +
            "FROM Question n " +
            "WHERE n.challenge.id = :challengeId")
    int getNextOrdinalNumber(@Param("challengeId") int challengeId);


    @Query("SELECT COUNT(n.id) FROM Question n WHERE n.challenge.id = :challengeId ")
    int countQuestionInChallenge(@Param("challengeId") int challengeId);
}
