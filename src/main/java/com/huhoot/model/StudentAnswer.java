package com.huhoot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.security.Timestamp;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.student", joinColumns = @JoinColumn(name = "student_id")),
        @AssociationOverride(name = "primaryKey.question", joinColumns = @JoinColumn(name = "question_id")),
        @AssociationOverride(name = "primaryKey.answer", joinColumns = @JoinColumn(name = "answer_id")),
        @AssociationOverride(name = "primaryKey.challenge", joinColumns = @JoinColumn(name = "challenge_id"))})
@EntityListeners({AuditingEntityListener.class})
public class StudentAnswer extends Auditable {
    @EmbeddedId
    @Getter
    private StudentAnswerId primaryKey = new StudentAnswerId();

    @Getter
    @Setter
    private float score;

    @Getter
    @Setter
    private boolean isCorrect;

    @Getter
    @Setter
    private Timestamp timeStamp;

    @Transient
    public Student getStudent() {
        return getPrimaryKey().getStudent();
    }

    public void setStudent(Student student) {
        getPrimaryKey().setStudent(student);
    }

    @Transient
    public Question getQuestion() {
        return getPrimaryKey().getQuestion();
    }

    public void setQuestion(Question question) {
        getPrimaryKey().setQuestion(question);
    }

}
