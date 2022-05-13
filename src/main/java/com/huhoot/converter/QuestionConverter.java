package com.huhoot.converter;

import com.huhoot.host.manage.question.QuestionAddRequest;
import com.huhoot.model.Question;

public class QuestionConverter {


    public static Question toEntity(QuestionAddRequest request) {
        Question question = new Question();
        question.setQuestionContent(request.getQuestionContent());
        question.setQuestionImage(request.getQuestionImage());
        question.setAnswerTimeLimit(request.getAnswerTimeLimit());
        question.setPoint(request.getPoint());
        question.setAnswerOption(request.getAnswerOption());
        return question;
    }


}
