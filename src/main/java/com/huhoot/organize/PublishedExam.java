package com.huhoot.organize;

import com.huhoot.dto.ChallengeResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PublishedExam {
    private ChallengeResponse challenge;
    private String questionToken;
    private PublishQuestion question;
    private List<AnswerResultResponse> answers;

}
