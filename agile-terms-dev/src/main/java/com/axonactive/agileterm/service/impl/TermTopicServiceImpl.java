package com.axonactive.agileterm.service.impl;

import com.axonactive.agileterm.entity.TermEntity;
import com.axonactive.agileterm.entity.TermTopicEntity;
import com.axonactive.agileterm.entity.TopicEntity;
import com.axonactive.agileterm.exception.ErrorMessage;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.repository.TermRepository;
import com.axonactive.agileterm.repository.TermTopicRepository;
import com.axonactive.agileterm.repository.TopicRepository;
import com.axonactive.agileterm.rest.client.model.TermTopic;
import com.axonactive.agileterm.rest.model.TermDto;
import com.axonactive.agileterm.rest.model.TermTopicDto;
import com.axonactive.agileterm.rest.model.TopicDto;
import com.axonactive.agileterm.service.DescriptionService;
import com.axonactive.agileterm.service.TermTopicService;
import com.axonactive.agileterm.service.TopicService;
import com.axonactive.agileterm.service.mapper.TermMapper;
import com.axonactive.agileterm.service.mapper.TermTopicMapper;
import com.axonactive.agileterm.service.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Service
public class TermTopicServiceImpl implements TermTopicService {

    @Autowired
    private ErrorMessage errorMessage;

    @Autowired
    private TermTopicRepository termTopicRepository;

    @Autowired
    private TermTopicMapper termTopicMapper;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private TermMapper termMapper;

    @Autowired
    private DescriptionService descriptionService;

    @Autowired
    private TopicService topicService;


    @Override
    public List<TermTopicDto> getAll() {
        return termTopicMapper.toDtos(termTopicRepository.findAll());
    }

    @Override
    public TermTopicDto findById(Integer id) {
        return termTopicMapper.toDto(termTopicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage.termTopicNotFoundMsg)));
    }

    @Override
    public TermTopicDto save(TermTopic input) {
        TermTopicEntity inputTermTopicEntity = convertTermTopicToTermTopicEntity(input);
        return termTopicMapper.toDto(termTopicRepository.save(inputTermTopicEntity));
    }

    public TermTopicEntity convertTermTopicToTermTopicEntity(TermTopic input) {
        TermEntity inputTerm = termRepository.findById(input.getTermId()).orElseThrow(() -> new ResourceNotFoundException(errorMessage.termNotFoundMsg));
        TopicEntity inputTopic = topicRepository.findById(input.getTopicId()).orElseThrow(() -> new ResourceNotFoundException(errorMessage.topicNotFoundMsg));

        return new TermTopicEntity(
                null, inputTerm, inputTopic);
    }

    @Override
    public List<TermDto> findTermByTopicId(Integer id) {
        return termMapper.toDtos(termTopicRepository.findTermByTopicId(id));
    }

    @Override
    public List<TopicDto> findTopicByTermId(Integer id) {
        return topicMapper.toDtos(termTopicRepository.findTopicByTermId(id));
    }

    @Override
    public List<TopicDto> findTopicByTermName(String name) {
        return topicMapper.toDtos(termTopicRepository.findTopicByTermName(name));
    }

    @Override
    public List<TermDto> findTermByTopicName(String name) {
        return termMapper.toDtos(termTopicRepository.findTermByTopicName(name));
    }

    @Override
    public TermDto findTermDetailById(String encodedId) {
        Integer id = getDecodedId(encodedId);

        TermDto foundTerm = termMapper.toDto(termRepository.findTermByTermId(id)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage.termNotFoundMsg)));
        List<TopicDto> topicDtoList = topicMapper.toDtos(termTopicRepository.findTopicByTermId(id));
        TermDto termDto = termMapper.toDto(termRepository.findTermByTermId(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage.termNotFoundMsg)));
        termDto.setTopicList(topicDtoList);

        return termDto;
    }


    @Override
    public List<TermTopicDto> saveAll(List<TermTopic> termTopicList) {
        List<TermTopicEntity> termTopicEntityList = new ArrayList<>();
        for (TermTopic termTopic : termTopicList) {
            termTopicEntityList.add(convertTermTopicToTermTopicEntity(termTopic));
        }
        return termTopicMapper.toDtos(termTopicRepository.saveAll(termTopicEntityList));
    }

    @Override
    public boolean isTopicListValid(List<Integer> topicIdList) {
        return topicIdList != null &&
                !topicIdList.isEmpty();
    }

    @Override
    public List<TermTopicEntity> saveAllTermTopic(TermEntity term, List<Integer> topicIdList) {
        List<TermTopicEntity> termTopicEntityList = new ArrayList<>();
        for (Integer topicId : topicIdList
        ) {
            termTopicEntityList.add(new TermTopicEntity(null, term, topicService.findTopicEntityById(topicId)));
        }
        return termTopicRepository.saveAllAndFlush(termTopicEntityList);
    }

    @Override
    public List<TopicEntity> findListOfTopicEntityFromTermId(Integer id) {
        return termTopicRepository.findTopicByTermId(id);
    }

    private Integer getDecodedId(String encodedId) {
        String decodedString;
        int termId;
        if (encodedId.length() < 2) {
            throw new ResourceNotFoundException(errorMessage.termNotFoundMsg);
        }
        try {
            decodedString = new String(Base64.getDecoder().decode(encodedId));
            termId = Integer.parseInt(decodedString.substring(decodedString.lastIndexOf('_') + 1));
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException(errorMessage.termNotFoundMsg);
        }

        return termId;
    }

}
