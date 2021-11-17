package com.huhoot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.student", joinColumns = @JoinColumn(name = "student_id")),
        @AssociationOverride(name = "primaryKey.challenge", joinColumns = @JoinColumn(name = "challenge_id"))})
@EntityListeners({AuditingEntityListener.class})
public class StudentChallenge extends Auditable {

    @Getter
    @Setter
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

    @Getter
    @Setter
    private boolean isDeleted;

    public StudentChallenge(Student student, Challenge challenge ){
        this.setStudent(student);
        this.setChallenge(challenge);
        this.isLogin = false;
        this.totalScore = 0;
        this.isKicked = false;
        this.isOnline = false;
        this.isDeleted = false;
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