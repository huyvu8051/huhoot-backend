package com.huhoot.mapper;


import com.huhoot.dto.QuestionResponse;
import com.huhoot.dto.QuestionUpdateRequest;
import com.huhoot.model.Question;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    //@Mapping(target = "nonDeleted", source = "isNonDeleted")
    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(QuestionUpdateRequest dto, @MappingTarget Question entity);


    QuestionResponse toDto(Question entity);
}