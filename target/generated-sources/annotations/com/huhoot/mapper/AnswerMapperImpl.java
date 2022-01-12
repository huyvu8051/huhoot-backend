package com.huhoot.mapper;

import com.huhoot.host.manage.answer.AnswerUpdateRequest;
import com.huhoot.model.Answer;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-01-11T08:08:16+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.13 (Ubuntu)"
)
@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Override
    public void updateAnswer(AnswerUpdateRequest dto, Answer entity) {
        if ( dto == null ) {
            return;
        }
    }
}
