package com.axonactive.agileterm.repository;

import com.axonactive.agileterm.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Integer> {
}

