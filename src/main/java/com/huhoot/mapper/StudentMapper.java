package com.huhoot.mapper;

import com.huhoot.admin.manage.student.StudentAddRequest;
import com.huhoot.admin.manage.student.StudentResponse;
import com.huhoot.admin.manage.student.StudentUpdateRequest;
import com.huhoot.model.Student;
import org.mapstruct.MappingTarget;


//@Mapper
public interface StudentMapper {

    // @Mapping(source = "nonLocked", target = "isNonLocked")
    StudentResponse toDto(Student entity);

    Student toEntity(StudentAddRequest request);

    //@Mapping(source = "isNonLocked", target = "nonLocked")
    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(StudentUpdateRequest request, @MappingTarget Student entity);
}
