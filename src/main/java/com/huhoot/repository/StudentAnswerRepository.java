package com.huhoot.repository;

import com.huhoot.dto.AnswerStatisticsResponse;
import com.huhoot.dto.StudentScoreResponse;
import com.huhoot.model.StudentAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {

    /**
     * @param challengeId challenge id
     * @param studentId   student id
     * @return total point of student answer
     */
    @Query("SELECT SUM(m.score) " +
            "FROM StudentAnswer m " +
            "WHERE m.primaryKey.challenge.id = :challengeId and m.primaryKey.student.id = :studentId")
    int getTotalPointInChallenge(int challengeId, int studentId);


    /**
     * @param challengeId challenge id
     * @param adminId     admin id
     * @param pageable    pageable
     * @return list of student id, fullName, score <br/>
     * Not contain <b>rank</b>
     */
    @Query("SELECT new com.huhoot.dto.StudentScoreResponse(m.primaryKey.student.id, SUM(m.score), m.primaryKey.student.fullName)  " +
            "FROM StudentAnswer m " +
            "WHERE m.primaryKey.challenge.id = :challengeId AND m.primaryKey.challenge.admin.id = :adminId " +
            "GROUP BY m.primaryKey.student.id, m.primaryKey.student.fullName " +
            "ORDER BY SUM(m.score) DESC")
    Page<StudentScoreResponse> findTopStudent(int challengeId, int adminId, Pageable pageable);


    /**
     * This method use StudentAnswer answerDate, maybe many StudentAnswer have same answerDate
     * so this method may leave out some record
     *
     * @param questionId question id
     * @param hostId     host id
     * @return List of answer contain number of student choose
     */
    @Query("SELECT new com.huhoot.dto.AnswerStatisticsResponse(m.id, m.ordinalNumber, m.answerContent, COUNT(n.answerDate), m.question.id) " +
            "FROM Answer m LEFT JOIN StudentAnswer n " +
            "ON m.id = n.primaryKey.answer.id " +
            "WHERE m.question.id = :questionId AND m.question.challenge.admin.id = :hostId " +
            "GROUP BY m.id, m.ordinalNumber, m.answerContent, m.question.id ")
    List<AnswerStatisticsResponse> findStatisticsByQuestionId(int questionId, int hostId);

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
    void updateAnswer(double point, boolean isAnswersCorrect, Timestamp now, int answerId, int studentId);
}
//