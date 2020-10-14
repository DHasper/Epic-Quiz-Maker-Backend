package com.quizmaker.backend.repositories;

import java.util.List;

import com.quizmaker.backend.models.QuestionText;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTextRepository extends CrudRepository<QuestionText, Integer> {

    List<QuestionText> findByQuizId(@Param("quiz_id") int qid);

}