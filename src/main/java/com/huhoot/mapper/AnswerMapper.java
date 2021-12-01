package com.huhoot.mapper;

import com.huhoot.dto.AnswerUpdateRequest;
import com.huhoot.model.Answer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface AnswerMapper {

    //@Mappings({
    //        @Mapping(target = "correct", source = "isCorrect"),
    //       @Mapping(target = "nonDeleted", source = "isNonDeleted")
    //})
    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAnswer(AnswerUpdateRequest dto, @MappingTarget Answer entity);
}