package com.axonactive.agileterm.repository;

import com.axonactive.agileterm.entity.DescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescriptionRepository extends JpaRepository<DescriptionEntity, Integer> {
    List<DescriptionEntity> findByTermIdOrderByVotePointDescCreateDateDesc(Integer id);

    @Query("SELECT d.content FROM DescriptionEntity d WHERE d.term.name=?1")
    List<String> findDescriptionContentByTermName(String name);


    @Query("SELECT d FROM DescriptionEntity d WHERE d.term.name=?1 AND LOWER(d.content) = LOWER(?2)")
    List<DescriptionEntity> findDescriptionByTermIdAndDescriptionString(String termString, String descriptionContent);

}
