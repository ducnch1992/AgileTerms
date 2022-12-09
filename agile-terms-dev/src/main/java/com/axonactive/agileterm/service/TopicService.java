package com.axonactive.agileterm.service;


import com.axonactive.agileterm.entity.TopicEntity;
import com.axonactive.agileterm.rest.client.model.Topic;
import com.axonactive.agileterm.rest.model.TopicDto;

import java.util.List;

public interface TopicService {
    List<TopicDto> getAll();

    TopicDto findById(Integer id);

    TopicDto save(Topic input);

    TopicDto update(Integer id, Topic input);

    void delete(Integer id);

    List<TopicEntity> getPopularTopic();

    TopicEntity findTopicEntityById(Integer id);
}
