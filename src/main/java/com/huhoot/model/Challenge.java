package com.huhoot.model;

import com.huhoot.enums.ChallengeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@Where(clause = "is_non_deleted=1")
public class Challenge extends Auditable {
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
    private List<StudentInChallenge> studentChallenges = new ArrayList<>();

    private boolean isNonDeleted;

    public Challenge() {
        this.isNonDeleted = true;
    }


}
