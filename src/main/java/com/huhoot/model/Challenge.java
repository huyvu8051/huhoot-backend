package com.huhoot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Data
public class Challenge extends Auditable{
	@Id
	@GeneratedValue
	private int id;

	private String title;

	private String coverImage;

	private boolean randomAnswer;

	private boolean randomQuest;

	private com.huhoot.enums.ChallengeStatus ChallengeStatus;

	@ManyToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;

	@OneToMany(mappedBy = "challenge")
	private List<Question> questions = new ArrayList<>();

	@OneToMany(mappedBy = "primaryKey.challenge")
	private List<StudentAnswer> studentAnswers = new ArrayList<>();

	@OneToMany(mappedBy = "primaryKey.challenge")
	private List<StudentChallenge> studentChallenges = new ArrayList<>();

	private boolean isDeleted;
}
