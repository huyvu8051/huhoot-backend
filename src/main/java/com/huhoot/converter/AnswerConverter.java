package com.huhoot.converter;

import com.huhoot.dto.AnswerAddRequest;
import com.huhoot.dto.PublishAnswer;
import com.huhoot.model.Answer;

public class AnswerConverter {
    public static PublishAnswer toAnswerResponse(Answer entity) {
        PublishAnswer response = new PublishAnswer();
        response.setId(entity.getId());
        response.setOrdinalNumber(entity.getOrdinalNumber());
        response.setAnswerContent(entity.getAnswerContent());
        response.setIsCorrect(entity.isCorrect());

        return response;
    }

    public static Answer toEntity(AnswerAddRequest request) {
        Answer answer = new Answer();
        answer.setOrdinalNumber(request.getOrdinalNumber());
        answer.setAnswerContent(request.getAnswerContent());
        answer.setCorrect(request.isCorrect());
        return answer;
    }

    public static PublishAnswer toPublishAnswerResponse(Answer answer) {
        return PublishAnswer.builder()
                .id(answer.getId())
                .ordinalNumber(answer.getOrdinalNumber())
                .answerContent(answer.getAnswerContent())
                .createdDate(answer.getCreatedDate().getTime())
                .isCorrect(answer.isCorrect())
                .build();
    }
}
