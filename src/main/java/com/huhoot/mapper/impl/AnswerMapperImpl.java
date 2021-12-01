package com.huhoot.mapper.impl;

import com.huhoot.dto.AnswerUpdateRequest;
import com.huhoot.mapper.AnswerMapper;
import com.huhoot.model.Answer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-11-30T22:58:18+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (Temurin)"
)
@Primary
@Component("myAnswerMapper")
public class AnswerMapperImpl implements AnswerMapper {

    @Override
    public void updateAnswer(AnswerUpdateRequest dto, Answer entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getIsCorrect() != null ) {
            entity.setCorrect( dto.getIsCorrect() );
        }
        if ( dto.getIsNonDeleted() != null ) {
            entity.setNonDeleted( dto.getIsNonDeleted() );
        }
        entity.setId( dto.getId() );
        if ( dto.getOrdinalNumber() != null ) {
            entity.setOrdinalNumber( dto.getOrdinalNumber() );
        }
        if ( dto.getAnswerContent() != null ) {
            entity.setAnswerContent( dto.getAnswerContent() );
        }
    }
}
