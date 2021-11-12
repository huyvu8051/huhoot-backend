package com.huhoot.repository;

import com.huhoot.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAllByQuestionChallengeAdminIdAndQuestionId(int id, int questionId);

    Answer findOneByIdAndQuestionChallengeAdminId(int answerId, int id);

    Answer findOneById(int id);

    List<Answer> findAllByIdIn(List<Integer> ids);
}
