package com.axonactive.agileterm.repository;

import com.axonactive.agileterm.entity.TermEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<TermEntity, Integer> {

    @Query("SELECT DISTINCT t FROM TermEntity t LEFT JOIN FETCH t.descriptionEntityList d")
    List<TermEntity> getAllQuery();

    @Query("SELECT DISTINCT t FROM TermEntity t LEFT JOIN FETCH t.descriptionEntityList d WHERE t.id = ?1 ")
    Optional<TermEntity> findTermByTermId(Integer id);

    @Query("SELECT DISTINCT t FROM TermEntity t LEFT JOIN FETCH t.descriptionEntityList d WHERE t.name = ?1")
    Optional<TermEntity> findTermByTermName(String name);

    @Query("SELECT t.name FROM TermEntity t")
    List<String> getAllTermName();

    @Query("SELECT t.name FROM TermEntity t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%',?1,'%')) ")
    List<String> get5TermNameLike(String searchInput);

    @Query("SELECT COUNT(t) FROM TermEntity t WHERE LOWER(t.name) = LOWER(?1)")
    Integer countTermByTermName (String searchInput);



    @Query("SELECT t FROM TermEntity t LEFT JOIN FETCH t.descriptionEntityList d LEFT JOIN FETCH d.userEntity ORDER BY t.id DESC")
    List<TermEntity> find10RecentTerms(Pageable pageable);


    @Query(value = "SELECT t.id FROM Term t LEFT JOIN description d ON t.id = d.term_id GROUP By t.id ORDER BY COUNT(d.id) DESC LIMIT 10",nativeQuery = true)
    List<Integer> get10PopularTermsId();

    @Query("SELECT DISTINCT t1 FROM TermEntity t1 LEFT JOIN FETCH t1.descriptionEntityList d1 LEFT JOIN FETCH d1.userEntity WHERE t1.id IN( ?1 )")
    List<TermEntity> findTermsByIdList (List<Integer> termIdList );
}

