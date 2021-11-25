package com.huhoot.repository;

import com.huhoot.dto.AnswerResponse;
import com.huhoot.dto.PublishAnswerResponse;
import com.huhoot.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    Optional<Answer> findOneByIdAndQuestionChallengeAdminId(int answerId, int id);

    Optional<Answer> findOneById(int id);

    List<Answer> findAllByIdIn(List<Integer> ids);

    List<Answer> findAllByQuestionChallengeAdminIdAndQuestionId(int id, int questionId);

    /**
     * @param questionId {@link com.huhoot.model.Question} id
     * @return List of {@link Integer} as correct {@link Answer} ids
     */
    @Query("SELECT a.id " +
            "FROM Answer a " +
            "WHERE a.question.id = :questionId AND a.isCorrect = TRUE")
    List<Integer> findAllCorrectAnswerIds(int questionId);

    /**
     * @param questionId {@link com.huhoot.model.Question} id
     * @return List of {@link PublishAnswerResponse}
     */
    @Query("SELECT new com.huhoot.dto.PublishAnswerResponse(n.id,n.ordinalNumber, n.answerContent) " +
            "FROM Answer n " +
            "WHERE n.question.id = :questionId")
    List<PublishAnswerResponse> findAllPublishAnswerResponseByQuestionId(int questionId);

    /**
     * @param questionId {@link com.huhoot.model.Question} id
     * @return List of {@link AnswerResponse}
     */
    @Query("SELECT new com.huhoot.dto.AnswerResponse(n.id, n.ordinalNumber, n.answerContent,n.isCorrect) FROM Answer n WHERE n.question.id = :questionId")
    List<AnswerResponse> findAllAnswerByQuestionIdAndAdminId(int questionId);
}
