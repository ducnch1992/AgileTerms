package com.axonactive.agileterm.service;

import com.axonactive.agileterm.entity.DescriptionEntity;
import com.axonactive.agileterm.entity.TermEntity;
import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.rest.client.model.Description;
import com.axonactive.agileterm.rest.model.DescriptionDto;

import java.util.List;
import java.util.Set;

public interface DescriptionService {

    List<DescriptionDto> getAll();

    DescriptionDto findById(Integer id);

    DescriptionDto save(Integer termId, Description description);

    void deleteById(Integer id);

    DescriptionDto updateById(Integer termId, Description description, Integer id);

    List<DescriptionDto> findByTermIdOrderByVotePointDescCreateDateDesc(Integer id);

    List<String> findDescriptionContentByTermName(String name);

    List<DescriptionEntity> convertDescriptionContentListToDescriptionList(Set<String> contentDescriptionList, TermEntity term, UserEntity userEntity);

    List<DescriptionEntity> findDescriptionByTermIdAndDescriptionString(String termName, String descriptionContent);

    boolean isDescriptionListValid(List<Description> descriptionList);

    List<DescriptionEntity> convertListOfDescriptionToListOfDescriptionEntity(List<Description> descriptionList, TermEntity term);

    DescriptionEntity convertDescriptionToDescriptionEntity(Description description, TermEntity term);

    List<DescriptionEntity> saveAll(List<DescriptionEntity> descriptionEntityList);
}
