package com.huhoot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.student", joinColumns = @JoinColumn(name = "student_id")),
        @AssociationOverride(name = "primaryKey.challenge", joinColumns = @JoinColumn(name = "challenge_id"))})
@EntityListeners({AuditingEntityListener.class})
public class StudentInChallenge extends Auditable {


    private boolean isLogin;

    @EmbeddedId

    private StudentInChallengeId primaryKey = new StudentInChallengeId();

    private int totalScore;

    private boolean isKicked;

    private boolean isOnline;

    private boolean isNonDeleted;

    public StudentInChallenge(Student student, Challenge challenge) {
        this.setStudent(student);
        this.setChallenge(challenge);
        this.isLogin = false;
        this.totalScore = 0;
        this.isKicked = false;
        this.isOnline = false;
        this.isNonDeleted = true;
    }


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