package com.huhoot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Where(clause="is_non_deleted=1")
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
	private List<StudentInChallenge> studentChallenges = new ArrayList<>();

	private boolean isNonDeleted;

	private UUID adminSocketId;

	public Challenge(){
		this.isNonDeleted = true;
	}
}
