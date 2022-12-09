package com.axonactive.agileterm.service.mapper;

import com.axonactive.agileterm.entity.TopicEntity;
import com.axonactive.agileterm.rest.model.TopicDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicDto toDto(TopicEntity topic);

    List<TopicDto> toDtos(List<TopicEntity> topicList);
}
