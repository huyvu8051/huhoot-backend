package com.huhoot.converter;

import com.huhoot.dto.QuestionAddRequest;
import com.huhoot.dto.QuestionResponse;
import com.huhoot.model.Question;

public class QuestionConverter {
    public static QuestionResponse toQuestionResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setOrdinalNumber(question.getOrdinalNumber());
        response.setQuestionContent(question.getQuestionContent());
        response.setAnswerTimeLimit(question.getAnswerTimeLimit());
        response.setPoint(question.getPoint());
        response.setAnswerOption(question.getAnswerOption());
        response.setAskDate(question.getAskDate());
        response.setDeleted(question.isDeleted());
        return response;
    }

    public static Question toEntity(QuestionAddRequest request) {
        Question question = new Question();
        question.setOrdinalNumber(request.getOrdinalNumber());
        question.setQuestionContent(request.getQuestionContent());
        question.setAnswerTimeLimit(request.getAnswerTimeLimit());
        question.setPoint(request.getPoint());
        question.setAnswerOption(request.getAnswerOption());
        return question;
    }

}
