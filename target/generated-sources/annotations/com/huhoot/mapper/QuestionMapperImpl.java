package com.huhoot.mapper;

import com.huhoot.host.manage.question.QuestionResponse;
import com.huhoot.host.manage.question.QuestionUpdateRequest;
import com.huhoot.model.Question;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-01-11T08:08:17+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.13 (Ubuntu)"
)
@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Override
    public void update(QuestionUpdateRequest dto, Question entity) {
        if ( dto == null ) {
            return;
        }
    }

    @Override
    public QuestionResponse toDto(Question entity) {
        if ( entity == null ) {
            return null;
        }

        QuestionResponse questionResponse = new QuestionResponse();

        return questionResponse;
    }
}
