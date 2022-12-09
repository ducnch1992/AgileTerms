package com.axonactive.agileterm.service.mapper;


import com.axonactive.agileterm.entity.DescriptionEntity;
import com.axonactive.agileterm.entity.TermEntity;
import com.axonactive.agileterm.rest.model.DescriptionDto;
import com.axonactive.agileterm.rest.model.TermDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TermMapper {

    @Mapping(target = "encodedId", expression = "java(java.util.Base64.getEncoder().encodeToString((term.getName()+\"_\"+term.getId()).getBytes()))")
    @Mapping(target = "descriptionList", source = "descriptionEntityList")
    TermDto toDto(TermEntity term);

    List<TermDto> toDtos(List<TermEntity> terms);

    @Mapping(target = "authorName", source = "userEntity.username")
    DescriptionDto toDescriptionDto(DescriptionEntity description);

    List<DescriptionDto> toDescriptionDtos(List<DescriptionEntity> descriptionList);
}
