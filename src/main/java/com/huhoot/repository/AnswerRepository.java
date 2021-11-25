package com.huhoot.repository;

import com.huhoot.dto.AnswerStatisticsResponse;
import com.huhoot.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    Optional<Answer> findOneByIdAndQuestionChallengeAdminId(int answerId, int id);

    Optional<Answer> findOneById(int id);

    List<Answer> findAllByIdIn(List<Integer> ids);

    List<Answer> findAllByAnswerContentContainingIgnoreCase(String s);

    List<Answer> findAllByQuestionChallengeAdminIdAndQuestionId(int id, int questionId);

    List<Answer> findAllByIdInAndIsCorrectTrue(List<Integer> answerIds);

    List<Answer> findAllByQuestionId(int questionId);

    @Query("SELECT a.id FROM Answer a WHERE a.question.id = :questionId AND a.isCorrect = TRUE")
    List<Integer> findAllCorrectAnswerIds(int questionId);
}
