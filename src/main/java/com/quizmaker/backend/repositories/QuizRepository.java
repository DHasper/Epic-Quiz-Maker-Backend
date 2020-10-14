package com.quizmaker.backend.repositories;

import java.util.List;
import java.util.Optional;

import com.quizmaker.backend.models.Quiz;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Integer> {

    List<Quiz> findTop10ByOrderByViewsDesc();

    List<Quiz> findTop10ByOrderByDateDesc();

    Optional<List<Quiz>> findAllByOrderByDateDesc();

    Optional<List<Quiz>> findAllByOrderByDateAsc();

    Optional<List<Quiz>> findAllByOrderByViewsAsc();

    Optional<List<Quiz>> findAllByOrderByViewsDesc();

}