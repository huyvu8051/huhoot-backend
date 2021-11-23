package com.huhoot.repository;

import com.huhoot.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Page<Question> findAllByChallengeIdAndChallengeAdminId(int challengeId, int id, Pageable pageable);

    Optional<Question> findOneById(int id);

    List<Question> findAllByIdIn(List<Integer> ids);

    List<Question> findAllByChallengeIdAndChallengeAdminId(int challengeId, int id);

    Optional<Question> findOneByIdAndChallengeAdminId(int questionId, int adminId);

    List<Question> findAllByChallengeId(int id);
}
