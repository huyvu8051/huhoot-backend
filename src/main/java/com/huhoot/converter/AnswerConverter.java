package com.huhoot.converter;

import com.huhoot.dto.AnswerAddRequest;
import com.huhoot.dto.AnswerResponse;
import com.huhoot.dto.PublishAnswerResponse;
import com.huhoot.model.Answer;

public class AnswerConverter {
    public static AnswerResponse toAnswerResponse(Answer entity) {
        AnswerResponse response = new AnswerResponse();
        response.setId(entity.getId());
        response.setOrdinalNumber(entity.getOrdinalNumber());
        response.setAnswerContent(entity.getAnswerContent());
        response.setCorrect(entity.isCorrect());

        return response;
    }

    public static Answer toEntity(AnswerAddRequest request) {
        Answer answer = new Answer();
        answer.setOrdinalNumber(request.getOrdinalNumber());
        answer.setAnswerContent(request.getAnswerContent());
        answer.setCorrect(request.isCorrect());
        return answer;
    }

    public static PublishAnswerResponse toPublishAnswerResponse(Answer answer) {
        PublishAnswerResponse response = new PublishAnswerResponse();
        response.setId(answer.getId());
        response.setOrdinalNumber(answer.getOrdinalNumber());
        response.setAnswerContent(answer.getAnswerContent());
        return response;
    }
}
