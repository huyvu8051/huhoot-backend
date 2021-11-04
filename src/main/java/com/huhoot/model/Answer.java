package com.huhoot.model;

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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({ AuditingEntityListener.class })
public class Answer {

	@Id
	@GeneratedValue
	private int id;

	private int ordinalNumber;

	private String answerContent;

	private boolean isCorrect;

	private boolean isDeleted;
	
	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date modifiedDate;

	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;

	@OneToMany(mappedBy = "primaryKey.answer", cascade = CascadeType.ALL)
	private List<StudentAnswer> studentAnswers = new ArrayList<>();

}
