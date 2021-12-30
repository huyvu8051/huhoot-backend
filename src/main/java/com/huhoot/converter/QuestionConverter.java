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
