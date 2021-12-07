package com.huhoot.model;

import com.huhoot.enums.Points;
import com.huhoot.enums.AnswerOption;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EntityListeners({AuditingEntityListener.class})
@Where(clause = "is_non_deleted=1")
public class Question extends Auditable {

    @Id
    @GeneratedValue
    private int id;

    private int ordinalNumber;


    @Column(columnDefinition = "nvarchar(255)")
    private String questionContent;

    private String questionImage;

    private int answerTimeLimit;

    private Points point;

    private AnswerOption answerOption;

    private Timestamp askDate;

    private boolean isNonDeleted;

    private byte[] encryptKey;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "primaryKey.question", cascade = CascadeType.ALL)
    private List<StudentAnswer> studentAnswers = new ArrayList<>();

    public Question() {
        this.isNonDeleted = true;
    }

    public Question(int id){
        this.id = id;
    }

}
