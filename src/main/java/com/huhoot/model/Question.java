package com.huhoot.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.huhoot.enums.AnswerTime;
import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@EntityListeners({ AuditingEntityListener.class })
@Where(clause="is_non_deleted=1")
public class Question extends Auditable{

	@Id
	@GeneratedValue
	private int id;
	
	private int ordinalNumber;

	private String questionContent;

	private int answerTimeLimit;

	private Points point;

	private AnswerOption answerOption;

	private Timestamp askDate;


	private boolean isNonDeleted;

	@ManyToOne
	@JoinColumn(name = "challenge_id")
	private Challenge challenge;

	@OneToMany(mappedBy = "question")
	private List<Answer> answers = new ArrayList<>();

	@OneToMany(mappedBy = "primaryKey.question", cascade = CascadeType.ALL)
	private List<StudentAnswer> studentAnswers = new ArrayList<>();

	public Question(){
		this.isNonDeleted = true;
	}

}
