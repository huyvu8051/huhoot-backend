package com.huhoot.repository;

import com.huhoot.dto.PublishQuestionResponse;
import com.huhoot.dto.QuestionResponse;
import com.huhoot.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Page<Question> findAllByChallengeIdAndChallengeAdminId(int challengeId, int id, Pageable pageable);

    Optional<Question> findOneById(int id);

    List<Question> findAllByIdIn(List<Integer> ids);

    Optional<Question> findOneByIdAndChallengeAdminId(int questionId, int adminId);

    List<Question> findAllByChallengeId(int id);

    Optional<Question> findOneByIdAndAskDateNotNull(int questionId);

    /**
     * @param challengeId challenge id
     * @param adminId     admin id
     * @return List of QuestionResponse(id, ordinalNumber, questionContent, answerTimeLimit, point, answerOption, askDate, isNonDeleted)
     */
    @Query("SELECT new com.huhoot.dto.QuestionResponse(n.id, n.ordinalNumber, n.questionContent, n.answerTimeLimit, n.point, n.answerOption, n.askDate, n.isNonDeleted) " +
            "FROM Question n " +
            "WHERE n.challenge.id = :challengeId AND n.challenge.admin.id = :adminId")
    List<QuestionResponse> findAllQuestionResponse(int challengeId, int adminId);

    //@Query("SELECT new com.huhoot.dto.PublishQuestionResponse(n.answers) FROM Question n")
    //List<PublishQuestionResponse> findAllPublishQuestionResponse(int questionId);
}
