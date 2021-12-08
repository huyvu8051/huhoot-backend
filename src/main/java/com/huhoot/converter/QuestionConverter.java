package com.huhoot.converter;

import com.huhoot.dto.PublishAnswer;
import com.huhoot.dto.PublishQuestion;
import com.huhoot.dto.QuestionAddRequest;
import com.huhoot.dto.QuestionResponse;
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
        response.setAskDate(question.getAskDate());
        response.setNonDeleted(question.isNonDeleted());
        response.setCreatedDate(question.getCreatedDate());
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

        for (Answer ans : question.getAnswers()){
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
