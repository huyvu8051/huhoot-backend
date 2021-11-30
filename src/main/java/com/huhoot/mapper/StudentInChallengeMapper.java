package com.huhoot.mapper;

import com.huhoot.dto.StudentInChallengeResponse;
import com.huhoot.model.StudentInChallenge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface StudentInChallengeMapper {
//    @Mappings({
//            @Mapping(source = "primaryKey.student.id", target = "studentId"),
//            @Mapping(source = "primaryKey.student.username", target = "studentUsername"),
//            @Mapping(source = "primaryKey.student.fullName", target = "studentFullName")
//    })
    StudentInChallengeResponse toDto(StudentInChallenge entity);
}
