package com.huhoot.mapper;

import com.huhoot.dto.StudentResponse;
import com.huhoot.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface StudentMapper {

    // @Mapping(source = "nonLocked", target = "isNonLocked")
    StudentResponse toDto(Student entity);
}
