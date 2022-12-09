package com.axonactive.agileterm.repository;

import com.axonactive.agileterm.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    @Query("SELECT COUNT(u.id) FROM UserEntity u WHERE u.email = ?1")
    Long countUsersWithEmail(String email);
    @Query("SELECT COUNT(u.id) FROM UserEntity u WHERE u.username = ?1")
    Long countUsersWithUsername(String username);
    Optional<UserEntity> findByUsername (String userName);
    Optional<UserEntity> findByVerificationTokenEntityVerificationCode (String code);

}
