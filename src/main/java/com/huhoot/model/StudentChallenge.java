package com.huhoot.model;

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
		@AssociationOverride(name = "primaryKey.challenge", joinColumns = @JoinColumn(name = "challenge_id")) })
@EntityListeners({ AuditingEntityListener.class })
public class StudentChallenge {

	private boolean isLogin;

	@EmbeddedId
	@Getter
	private StudentChallengeId primaryKey = new StudentChallengeId();
	
	@Getter
	@Setter
	private int totalScore;
	
	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date modifiedDate;
	
	@Getter
	@Setter
	private boolean isKicked;
	
	@Getter
	@Setter
	private boolean isOnline;

	@Transient
	public Student getStudent() {
		return getPrimaryKey().getStudent();
	}

	public void setStudent(Student student) {
		getPrimaryKey().setStudent(student);
	}

	@Transient
	public Challenge getChallenge() {
		return primaryKey.getChallenge();
	}

	public void setChallenge(Challenge challenge) {
		getPrimaryKey().setChallenge(challenge);
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

}