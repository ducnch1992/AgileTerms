package com.axonactive.agileterm.service;

import com.axonactive.agileterm.entity.TermEntity;
import com.axonactive.agileterm.entity.TermTopicEntity;
import com.axonactive.agileterm.entity.TopicEntity;
import com.axonactive.agileterm.rest.client.model.TermTopic;
import com.axonactive.agileterm.rest.model.TermDto;
import com.axonactive.agileterm.rest.model.TermTopicDto;
import com.axonactive.agileterm.rest.model.TopicDto;

import java.util.List;

public interface TermTopicService {
    List<TermTopicDto> getAll();

    TermTopicDto findById(Integer id);

    TermTopicDto save(TermTopic input);

    List<TermDto> findTermByTopicId(Integer id);

    List<TopicDto> findTopicByTermId(Integer id);

    List<TopicDto> findTopicByTermName(String name);

    List<TermDto> findTermByTopicName(String name);

    TermDto findTermDetailById(String encodedId);

    List<TermTopicDto> saveAll(List<TermTopic> termTopicList);

    boolean isTopicListValid(List<Integer> topicIdList);

    List<TermTopicEntity> saveAllTermTopic(TermEntity term, List<Integer> topicIdList);

    List<TopicEntity> findListOfTopicEntityFromTermId(Integer id);
}
