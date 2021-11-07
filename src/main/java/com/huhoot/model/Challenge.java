package com.huhoot.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Data
public class Challenge {
	@Id
	@GeneratedValue
	private int id;

	private String title;

	private String coverImage;

	private boolean randomAnswer;

	private boolean randomQuest;

	private ChallengeStatus ChallengeStatus;

	@ManyToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;

	@OneToMany(mappedBy = "challenge")
	private List<Question> questions = new ArrayList<>();

	@OneToMany(mappedBy = "primaryKey.challenge")
	private List<StudentAnswer> studentAnswers = new ArrayList<>();

	@OneToMany(mappedBy = "primaryKey.challenge")
	private List<StudentChallenge> studentChallenges = new ArrayList<>();


	@CreatedDate
	private Date createdDate;

	@CreatedBy
	private String createdBy;

	@LastModifiedDate
	private Date modifiedDate;

	@LastModifiedBy
	private String modifiedBy;

	private boolean isDeleted;
}
