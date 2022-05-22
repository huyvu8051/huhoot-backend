package com.huhoot.socket;

import com.huhoot.organize.PublishedExam;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipateJoinSuccessRes {
    private double totalPoints;
    private PublishedExam currentExam;
}
