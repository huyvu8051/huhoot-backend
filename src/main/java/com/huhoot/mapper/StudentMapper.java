package com.huhoot.mapper;

import com.huhoot.dto.StudentAddRequest;
import com.huhoot.dto.StudentResponse;
import com.huhoot.dto.StudentUpdateRequest;
import com.huhoot.model.Student;
import org.mapstruct.*;


@Mapper
public interface StudentMapper {

    // @Mapping(source = "nonLocked", target = "isNonLocked")
    StudentResponse toDto(Student entity);

    Student toEntity(StudentAddRequest request);

    //@Mapping(source = "isNonLocked", target = "nonLocked")
    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(StudentUpdateRequest request,@MappingTarget Student entity);
}
