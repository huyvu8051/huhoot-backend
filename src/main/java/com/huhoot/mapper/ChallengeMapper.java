package com.huhoot.mapper;

import com.huhoot.dto.ChallengeAddRequest;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.dto.ChallengeUpdateRequest;
import com.huhoot.model.Challenge;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ChallengeMapper {
    //@Mapping(target = "nonDeleted", source = "isNonDeleted")
    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(ChallengeUpdateRequest dto, @MappingTarget Challenge entity);

    //@Mapping(target = "owner", source = "admin.username")
    ChallengeResponse toDto(Challenge entity);

    Challenge toEntity(ChallengeAddRequest dto);
}