package com.quizmaker.backend.repositories;

import java.util.List;

import com.quizmaker.backend.models.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    
    List<User> findByUsername(@Param("username") String username);

    boolean existsByUsername(@Param("username") String username);
}