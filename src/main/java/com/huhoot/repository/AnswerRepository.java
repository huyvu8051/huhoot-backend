package com.huhoot.repository;

import com.huhoot.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    Optional<Answer> findOneByIdAndQuestionChallengeAdminId(int answerId, int id);

    Optional<Answer> findOneById(int id);

    List<Answer> findAllByIdIn(List<Integer> ids);

    List<Answer> findAllByAnswerContentContainingIgnoreCase(String s);

    List<Answer> findAllByQuestionChallengeAdminIdAndQuestionId(int id, int questionId);
}
