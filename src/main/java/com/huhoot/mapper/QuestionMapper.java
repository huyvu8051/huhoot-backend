package com.huhoot.mapper;


import com.huhoot.host.manage.question.QuestionResponse;
import com.huhoot.host.manage.question.QuestionUpdateRequest;
import com.huhoot.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    //@Mapping(target = "nonDeleted", source = "isNonDeleted")
    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(QuestionUpdateRequest dto, @MappingTarget Question entity);

    QuestionResponse toDto(Question entity);
}