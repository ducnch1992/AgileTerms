package com.axonactive.agileterm.repository;

import com.axonactive.agileterm.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Integer> {

    @Query(value = "SELECT  t1.id, t1.name , t1.color " +
            "FROM topic t1 " +
            "LEFT JOIN term_topic t2 " +
            "ON t1.id = t2.topic_id " +
            "GROUP BY t2.topic_id, t1.name, t1.id, t1.color " +
            "ORDER BY COUNT(t2.topic_id) DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<TopicEntity> findPopularTopics();

}
