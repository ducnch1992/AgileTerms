package com.axonactive.agileterm.service.mapper;

import com.axonactive.agileterm.entity.TermTopicEntity;
import com.axonactive.agileterm.rest.model.TermTopicDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TermTopicMapper {
    @Mapping(source = "term.id", target = "termId")
    @Mapping(source = "term.name", target = "termName")
    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "topic.name", target = "topicName")
    @Mapping(source = "topic.color", target = "topicColor")
    TermTopicDto toDto(TermTopicEntity termTopicEntity);

    List<TermTopicDto> toDtos(List<TermTopicEntity> termTopicEntityList);
}
