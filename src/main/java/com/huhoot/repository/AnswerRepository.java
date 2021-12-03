package com.huhoot.repository;

import com.huhoot.dto.PublishAnswer;
import com.huhoot.model.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    Optional<Answer> findOneByIdAndQuestionChallengeAdminId(int answerId, int id);

    Optional<Answer> findOneById(int id);

    List<Answer> findAllByIdIn(List<Integer> ids);

    Page<Answer> findAllByQuestionChallengeAdminIdAndQuestionId(int id, int questionId, Pageable pageable);

    /**
     * @param questionId {@link com.huhoot.model.Question} id
     * @return List of {@link Integer} as correct {@link Answer} ids
     */
    @Query("SELECT a.id " +
            "FROM Answer a " +
            "WHERE a.question.id = :questionId AND a.isCorrect = TRUE")
    List<Integer> findAllCorrectAnswerIds(@Param("questionId") int questionId);

    /**
     * @param questionId {@link com.huhoot.model.Question} id
     * @return List of {@link PublishAnswer}
     */
    @Query("SELECT new com.huhoot.dto.PublishAnswer(n.id,n.ordinalNumber, n.answerContent) " +
            "FROM Answer n " +
            "WHERE n.question.id = :questionId")
    List<PublishAnswer> findAllPublishAnswerByQuestionId(@Param("questionId") int questionId);

    /**
     * @param questionId {@link com.huhoot.model.Question} id
     * @return List of {@link PublishAnswer}
     */
    @Query("SELECT new com.huhoot.dto.PublishAnswer(n.id, n.ordinalNumber, n.answerContent,n.isCorrect) FROM Answer n WHERE n.question.id = :questionId")
    List<PublishAnswer> findAllAnswerByQuestionIdAndAdminId(@Param("questionId") int questionId);
}
