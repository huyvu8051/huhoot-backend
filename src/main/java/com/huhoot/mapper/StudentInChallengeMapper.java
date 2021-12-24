package com.huhoot.mapper;

import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeUpdateRequest;
import com.huhoot.model.StudentInChallenge;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface StudentInChallengeMapper {
    //    @Mappings({
//            @Mapping(source = "primaryKey.student.id", target = "studentId"),
//            @Mapping(source = "primaryKey.student.username", target = "studentUsername"),
//            @Mapping(source = "primaryKey.student.fullName", target = "studentFullName")
//    })
    StudentInChallengeResponse toDto(StudentInChallenge entity);

//    @Mappings({
//            @Mapping(source = "isKicked", target = "kicked"),
//            @Mapping(source = "isLogin", target = "login"),
//    })
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(StudentInChallengeUpdateRequest dto,@MappingTarget StudentInChallenge entity);
}
