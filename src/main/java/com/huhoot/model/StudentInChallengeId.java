package com.huhoot.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Embeddable
@Data
public class StudentInChallengeId implements Serializable {

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "challenge_id")
	private Challenge challenge;
}
