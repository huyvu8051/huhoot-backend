package com.huhoot.model;

import java.security.Timestamp;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Entity
@AssociationOverrides({
		@AssociationOverride(name = "primaryKey.student", joinColumns = @JoinColumn(name = "student_id")),
		@AssociationOverride(name = "primaryKey.question", joinColumns = @JoinColumn(name = "question_id")),
		@AssociationOverride(name = "primaryKey.answer", joinColumns = @JoinColumn(name = "answer_id")),
		@AssociationOverride(name = "primaryKey.challenge", joinColumns = @JoinColumn(name = "challenge_id"))})
@EntityListeners({ AuditingEntityListener.class })
public class StudentAnswer {
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
	
	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date modifiedDate;

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
