package com.huhoot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.student", joinColumns = @JoinColumn(name = "student_id")),
        @AssociationOverride(name = "primaryKey.challenge", joinColumns = @JoinColumn(name = "challenge_id"))})
@EntityListeners({AuditingEntityListener.class})
public class StudentChallenge extends Auditable {

    private boolean isLogin;

    @EmbeddedId
    @Getter
    private StudentChallengeId primaryKey = new StudentChallengeId();

    @Getter
    @Setter
    private int totalScore;

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