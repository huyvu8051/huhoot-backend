package com.huhoot.converter;

import com.huhoot.host.manage.question.QuestionAddRequest;
import com.huhoot.host.manage.question.QuestionResponse;
import com.huhoot.host.organize.PublishAnswer;
import com.huhoot.host.organize.PublishQuestion;
import com.huhoot.model.Answer;
import com.huhoot.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionConverter {
    public static QuestionResponse toQuestionResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setOrdinalNumber(question.getOrdinalNumber());
        response.setQuestionContent(question.getQuestionContent());
        response.setQuestionImage(question.getQuestionImage());
        response.setAnswerTimeLimit(question.getAnswerTimeLimit());
        response.setPoint(question.getPoint());
        response.setAnswerOption(question.getAnswerOption());
        if (question.getAskDate() != null)
            response.setAskDate(question.getAskDate().getTime());
        response.setNonDeleted(question.isNonDeleted());
        if (question.getCreatedDate() != null)
            response.setCreatedDate(question.getCreatedDate().getTime());
        return response;
    }

    public static Question toEntity(QuestionAddRequest request) {
        Question question = new Question();
        question.setOrdinalNumber(request.getOrdinalNumber());
        question.setQuestionContent(request.getQuestionContent());
        question.setQuestionImage(request.getQuestionImage());
        question.setAnswerTimeLimit(request.getAnswerTimeLimit());
        question.setPoint(request.getPoint());
        question.setAnswerOption(request.getAnswerOption());
        return question;
    }

    public static PublishQuestion toPublishQuestionResponse(Question question) {

        List<PublishAnswer> publishAnswerResponses = new ArrayList<>();

        for (Answer ans : question.getAnswers()) {
            publishAnswerResponses.add(AnswerConverter.toPublishAnswerResponse(ans));
        }

        PublishQuestion response = new PublishQuestion();

        response.setId(question.getId());
        response.setOrdinalNumber(question.getOrdinalNumber());
        response.setQuestionContent(question.getQuestionContent());
        response.setAnswerTimeLimit(question.getAnswerTimeLimit());
        response.setPoint(question.getPoint());
        response.setAnswerOption(question.getAnswerOption());
        response.setAskDate(question.getAskDate().getTime());

        return response;


    }
}
