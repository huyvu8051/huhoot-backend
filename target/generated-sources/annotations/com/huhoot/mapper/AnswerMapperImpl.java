package com.huhoot.mapper;

import com.huhoot.host.manage.answer.AnswerUpdateRequest;
import com.huhoot.model.Answer;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-01-23T22:12:10+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.1 (Private Build)"
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
