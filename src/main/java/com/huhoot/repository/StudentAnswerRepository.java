package com.huhoot.repository;

import com.huhoot.organize.AnswerResultResponse;
import com.huhoot.organize.StudentScoreResponse;
import com.huhoot.model.StudentAnswer;
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

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {

    /**
     * @param challengeId challenge id
     * @param studentId   student id
     * @return total point of student answer
     */
    @Query("SELECT  COALESCE(SUM(m.score), 0) " +
            "FROM StudentAnswer m " +
            "WHERE m.primaryKey.challenge.id = :challengeId and m.primaryKey.student.id = :studentId")
    double getTotalPointInChallenge(@Param("challengeId")int challengeId,
                                 @Param("studentId") int studentId);


    /**
     * @param challengeId challenge id
     * @param adminId     admin id
     * @param pageable    pageable
     * @return list of student id, fullName, score <br/>
     * Not contain <b>rank</b>
     */
    @Query("SELECT new com.huhoot.organize.StudentScoreResponse(m.primaryKey.student.id, SUM(m.score), m.primaryKey.student.fullName, m.primaryKey.student.username)  " +
            "FROM StudentAnswer m " +
            "WHERE m.primaryKey.challenge.id = :challengeId " +
            "GROUP BY m.primaryKey.student.id, m.primaryKey.student.fullName, m.primaryKey.student.username " +
            "ORDER BY SUM(m.score) DESC")
    Page<StudentScoreResponse> findTopStudent(@Param("challengeId")int challengeId,
                                              Pageable pageable);






    /**
     * A method for update student answer
     * use update instead insert because of performance
     *
     * @param point            point
     * @param isAnswersCorrect is the answer is correct or not
     * @param now              current timestamp
     * @param answerId         answer id
     * @param studentId        student id
     */
    @Modifying
    @Transactional
    @Query("UPDATE StudentAnswer s " +
            "SET s.score = :point, s.isCorrect = :isAnswersCorrect, s.answerDate = :now " +
            "WHERE s.primaryKey.answer.id = :answerId AND s.primaryKey.student.id = :studentId")
    void updateAnswer(@Param("point")double point,
                      @Param("isAnswersCorrect") boolean isAnswersCorrect,
                      @Param("now") Timestamp now,
                      @Param("answerId")int answerId,
                      @Param("studentId") int studentId);

    @Transactional
    @Modifying
    @Query("UPDATE StudentAnswer q " +
            "SET q.score = :point, q.isCorrect = :isCorrect, q.answerDate = :answerDate " +
            "WHERE q.primaryKey.answer.id IN (:ids) AND q.primaryKey.student.id = :studentId")
    void updateAnswerPoint(@Param("ids") List<Integer> ids,@Param("studentId") int studentId,@Param("point") double point,@Param("isCorrect") boolean isCorrect,@Param("answerDate") long answerDate);

    @Query("SELECT COUNT(DISTINCT a.primaryKey.student.id) FROM StudentAnswer a WHERE a.primaryKey.question.id = :questionId AND a.isCorrect = :isCorrect GROUP BY a.primaryKey.student.id")
    Optional<Integer> getTotalStudentAnswerByQuestIdAndIsCorrect(@Param("questionId") int questionId, @Param("isCorrect") Boolean isCorrect);


}
//