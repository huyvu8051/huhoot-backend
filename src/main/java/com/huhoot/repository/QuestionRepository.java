package com.huhoot.repository;

import com.huhoot.dto.PublishQuestionResponse;
import com.huhoot.dto.QuestionResponse;
import com.huhoot.model.Challenge;
import com.huhoot.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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
    @Query("SELECT new com.huhoot.dto.QuestionResponse(n.id, n.ordinalNumber, n.questionContent, n.answerTimeLimit, n.point, n.answerOption, n.askDate, n.isNonDeleted) " +
            "FROM Question n " +
            "WHERE n.challenge.id = :challengeId AND n.challenge.admin.id = :adminId")
    List<QuestionResponse> findAllQuestionResponse(int challengeId, int adminId);

    /**
     * @param questionId question id
     * @param adminId    admin id
     * @return List of PublishQuestionResponse
     */
    @Query("SELECT new com.huhoot.dto.PublishQuestionResponse(n.id, n.ordinalNumber, n.questionContent, n.answerTimeLimit, n.point, n.answerOption, n.challenge.id) " +
            "FROM Question n " +
            "WHERE n.id = :questionId AND n.challenge.admin.id = :adminId")
    Optional<PublishQuestionResponse> findAllPublishQuestionResponse(int questionId, int adminId);


    /**
     * @param askDate    ask date
     * @param questionId question id
     */
    @Transactional
    @Modifying
    @Query("UPDATE Question q " +
            "SET q.askDate = :askDate " +
            "WHERE q.id = :questionId")
    void updateAskDateByQuestionId(Timestamp askDate, int questionId);



}
