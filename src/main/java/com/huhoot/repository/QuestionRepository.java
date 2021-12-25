package com.huhoot.repository;

import com.huhoot.host.manage.question.QuestionResponse;
import com.huhoot.host.organize.PublishQuestion;
import com.huhoot.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Page<Question> findAllByChallengeIdAndChallengeAdminId(int challengeId, int id, Pageable pageable);

    Optional<Question> findOneById(int id);

    List<Question> findAllByIdIn(List<Integer> ids);

    List<Question> findAllByChallengeId(int id);

    Optional<Question> findOneByIdAndAskDateNotNull(int questionId);

    /**
     * @param challengeId challenge id
     * @param adminId     admin id
     * @return List of QuestionResponse(id, ordinalNumber, questionContent, answerTimeLimit, point, answerOption, askDate, isNonDeleted)
     */
    @Query("SELECT new com.huhoot.host.manage.question.QuestionResponse(n.id, n.ordinalNumber, n.questionContent, n.questionImage, n.answerTimeLimit, n.point, n.answerOption, n.askDate, n.isNonDeleted) " +
            "FROM Question n " +
            "WHERE n.challenge.id = :challengeId AND n.challenge.admin.id = :adminId")
    List<QuestionResponse> findAllQuestionResponse(@Param("challengeId") int challengeId, @Param("adminId") int adminId);

    /**
     * @param questionId question id
     * @param adminId    admin id
     * @return List of PublishQuestionResponse
     */
    @Query("SELECT new com.huhoot.host.organize.PublishQuestion(n.id, n.ordinalNumber, n.questionContent, n.questionImage, n.answerTimeLimit, n.point, n.answerOption, n.challenge.id, n.challenge.questions.size) " +
            "FROM Question n " +
            "WHERE n.id = :questionId AND n.challenge.admin.id = :adminId")
    Optional<PublishQuestion> findAllPublishQuestion(@Param("questionId") int questionId, @Param("adminId") int adminId);


    /**
     * @param askDate    ask date
     * @param questionId question id
     */
    @Transactional
    @Modifying
    @Query("UPDATE Question q " +
            "SET q.askDate = :askDate, q.encryptKey = :byteKey " +
            "WHERE q.id = :questionId")
    void updateAskDateAndEncryptKeyByQuestionId(@Param("askDate") Timestamp askDate,
                                                @Param("byteKey") byte[] byteKey,
                                                @Param("questionId") int questionId);



    Optional<Question> findFirstByChallengeIdAndChallengeAdminIdAndAskDateNullOrderByOrdinalNumberAsc(int challengeId, int adminId);

    @Query("SELECT COUNT(n.id) FROM Question n WHERE n.challenge.id = :challengeId AND n.askDate IS NOT NULL")
    int findNumberOfPublishedQuestion(@Param("challengeId") int challengeId);

    @Query("SELECT new com.huhoot.host.manage.question.QuestionResponse(n.id, n.ordinalNumber, n.questionContent, " +
            "n.questionImage, n.answerTimeLimit, n.point, n.answerOption, n.askDate, n.isNonDeleted, n.createdDate, " +
            "n.createdBy, n.modifiedDate, n.modifiedBy) " +
            "FROM Question n " +
            "WHERE n.challenge.id = :challengeId " +
            "AND n.challenge.admin.id = :adminId")
    Page<QuestionResponse> findAllByChallengeIdAndAdminId(@Param("challengeId") int challengeId, @Param("admin") int adminId, Pageable pageable);
}
