package com.quizmaker.backend.controllers;

import java.util.Optional;

import com.quizmaker.backend.models.Quiz;
import com.quizmaker.backend.repositories.QuizRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that handles all routing that deal with statistics.
 */
@RestController
@CrossOrigin()
public class StatisticsController {
    
    @Autowired
    private QuizRepository quizRepository;

    /**
     * This route is used to update certains statistics of a quiz.
     * 
     * @param qid of quiz to be updated.
     * @param type used to determine what stat to update.
     * @return 
     */
    @PatchMapping("/quiz/{qid}/statistics")
    public ResponseEntity<String> updateStatistics(@PathVariable final int qid
                        , @RequestParam(name = "type") final int type) {
        
        Optional<Quiz> quizResult = quizRepository.findById(qid);
        if(quizResult.isPresent()){

            Quiz quiz = quizResult.get();

            switch(type) {
                case 0:
                    // add a view
                    quiz.setViews(quiz.getViews() + 1);
                    quizRepository.save(quiz);
                    break;
                case 1:
                    // add a completion
                    quiz.setCompletions(quiz.getCompletions() + 1);
                    quizRepository.save(quiz);
                    break;
                default:
                    // BAD REQUEST if not a valid type
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // OK
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        // NOT FOUND when no quiz found with given quiz id
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}