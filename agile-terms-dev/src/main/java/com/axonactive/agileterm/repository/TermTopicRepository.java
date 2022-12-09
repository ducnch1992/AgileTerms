package com.axonactive.agileterm.repository;

import com.axonactive.agileterm.entity.TermEntity;
import com.axonactive.agileterm.entity.TermTopicEntity;
import com.axonactive.agileterm.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TermTopicRepository extends JpaRepository<TermTopicEntity, Integer> {

    @Query("SELECT t.topic FROM TermTopicEntity t WHERE t.term.name = ?1")
    List<TopicEntity> findTopicByTermName(String name);

    @Query("SELECT DISTINCT t.term FROM TermTopicEntity t LEFT JOIN FETCH t.term.descriptionEntityList d WHERE t.topic.name = ?1")
    List<TermEntity> findTermByTopicName(String name);

    @Query("SELECT  t.topic FROM TermTopicEntity t WHERE t.term.id = ?1")
    List<TopicEntity> findTopicByTermId(Integer id);

    @Query("SELECT DISTINCT t.term FROM TermTopicEntity t LEFT JOIN FETCH t.term.descriptionEntityList d WHERE t.topic.id = ?1")
    List<TermEntity> findTermByTopicId(Integer id);

    @Query("SELECT DISTINCT t.term FROM TermTopicEntity t LEFT JOIN FETCH t.term.descriptionEntityList d WHERE t.term.id = ?1")
    TermEntity findTermByTermId(Integer id);
}
