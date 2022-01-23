package com.huhoot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.student", joinColumns = @JoinColumn(name = "student_id")),
        @AssociationOverride(name = "primaryKey.question", joinColumns = @JoinColumn(name = "question_id")),
        @AssociationOverride(name = "primaryKey.answer", joinColumns = @JoinColumn(name = "answer_id")),
        @AssociationOverride(name = "primaryKey.challenge", joinColumns = @JoinColumn(name = "challenge_id"))})
@EntityListeners({AuditingEntityListener.class})
@SuperBuilder
public class StudentAnswer extends Auditable {
    @EmbeddedId
    @Getter
    private StudentAnswerId primaryKey = new StudentAnswerId();

    @Getter
    @Setter
    private Double score;

    @Getter
    @Setter
    private Boolean isCorrect;

    @Getter
    @Setter
    private Long answerDate;

    public StudentAnswer() {
        super();
    }


    @Transient
    public Student getStudent() {
        return getPrimaryKey().getStudent();
    }

    public void setStudent(Student student) {
        getPrimaryKey().setStudent(student);
    }

    public void setAnswer(Answer ans) {
        primaryKey.setAnswer(ans);
    }

    public void setChallenge(Challenge chal) {
        primaryKey.setChallenge(chal);
    }

    @Transient
    public Question getQuestion() {
        return getPrimaryKey().getQuestion();
    }

    public void setQuestion(Question question) {
        getPrimaryKey().setQuestion(question);
    }


    @Transient
    public Answer getAnswer() {
        return primaryKey.getAnswer();
    }

    @Transient
    public Challenge getChallenge(){
        return primaryKey.getChallenge();
    }


}
