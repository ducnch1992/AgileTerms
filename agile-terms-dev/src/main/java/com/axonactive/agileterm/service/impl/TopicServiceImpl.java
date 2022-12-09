package com.axonactive.agileterm.service.impl;

import com.axonactive.agileterm.entity.TopicEntity;
import com.axonactive.agileterm.exception.ErrorMessage;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.repository.TopicRepository;
import com.axonactive.agileterm.rest.client.model.Topic;
import com.axonactive.agileterm.rest.model.TopicDto;
import com.axonactive.agileterm.service.TopicService;
import com.axonactive.agileterm.service.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private ErrorMessage errorMessage;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public List<TopicDto> getAll() {
        return topicMapper.toDtos(topicRepository.findAll());
    }

    @Override
    public TopicDto findById(Integer id) {
        return topicMapper.toDto(topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage.topicNotFoundMsg)));
    }

    @Override
    public TopicDto save(Topic input) {
        return topicMapper.toDto(topicRepository.save(new TopicEntity(null, input.getName(), input.getColor())));
    }

    @Override
    public TopicDto update(Integer id, Topic input) {
        TopicEntity updatingTopic = topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage.topicNotFoundMsg));
        updatingTopic.setName(input.getName());
        updatingTopic.setColor(input.getColor());
        return topicMapper.toDto(topicRepository.save(updatingTopic));
    }

    @Override
    public void delete(Integer id) {
        topicRepository.deleteById(id);
    }

    @Override
    public List<TopicEntity> getPopularTopic() {
        return topicRepository.findPopularTopics();
    }

    @Override
    public TopicEntity findTopicEntityById(Integer id) {
        return topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage.topicNotFoundMsg));
    }


}
