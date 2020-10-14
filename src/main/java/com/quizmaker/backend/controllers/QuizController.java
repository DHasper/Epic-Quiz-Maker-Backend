package com.quizmaker.backend.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import com.quizmaker.backend.models.Category;
import com.quizmaker.backend.models.QuestionText;
import com.quizmaker.backend.models.Quiz;
import com.quizmaker.backend.repositories.CategoryRepository;
import com.quizmaker.backend.repositories.QuestionTextRepository;
import com.quizmaker.backend.repositories.QuizRepository;
import com.quizmaker.backend.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionTextRepository questionTextRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // used to make sure API is available
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getAPI() {
        return ResponseEntity.status(HttpStatus.OK).body(
            Collections.singletonMap("message", "API is running!")
        );
    }
    
    // return a quiz using a quiz_id
    @GetMapping("/quiz/{qid}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable final int qid) {

        // check if quiz exists
        Optional<Quiz> quizResult = quizRepository.findById(qid);

        if(quizRepository.existsById(qid) && quizResult.isPresent()){
            Quiz quiz = quizResult.get();

            // OK
            return ResponseEntity.status(HttpStatus.OK).body(quiz);
        }

        // NOT FOUND if no quiz found with qid
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // return all questions for a quiz with quiz_id
    @GetMapping("/quiz/{qid}/question")
    public ResponseEntity<ArrayList <QuestionText>> getQuizQuestions(@PathVariable final int qid) {
       
        ArrayList<QuestionText> result = new ArrayList<>();

        List<QuestionText> queryResult = questionTextRepository.findByQuizId(qid);
        Iterator<QuestionText> it = queryResult.iterator();
        while (it.hasNext()){
            QuestionText question = it.next();
            result.add(question);
        }

        if(result.isEmpty()){
            // NOT FOUND if no questions found for quiz_id
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } 

        // OK
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // return a question for quiz at specific position
    @GetMapping("/quiz/{qid}/question/{pos}")
    public ResponseEntity<QuestionText> getQuizQuestion(@PathVariable(name = "qid") final int qid
                                    , @PathVariable(name = "pos") final int pos) {

        List<QuestionText> questions = questionTextRepository.findByQuizId(qid);
        if(questions.isEmpty()){
            // NOT FOUND if no quiz found with qid
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        for(QuestionText question : questions){
            if(question.getPosition() == pos){
                // OK
                return ResponseEntity.status(HttpStatus.OK).body(question);
            }
        }

        // NOT FOUND if no question with position for given qid
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // return a list of popular quizzes
    @GetMapping("/quiz/popular")
    public ResponseEntity<List<Quiz>> getPopular() {
        List<Quiz> result = quizRepository.findTop10ByOrderByViewsDesc();

        if(result.isEmpty()){
            // NOT FOUND if top 10 cant be found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        // OK
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // return a list of new quizzes
    @GetMapping("/quiz/new")
    public ResponseEntity<List<Quiz>> getNew() {
        List<Quiz> result = quizRepository.findTop10ByOrderByDateDesc();

        if(result.isEmpty()){
            // NOT FOUND if top 10 cant be found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        // OK
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // returns available categories
    @GetMapping("quiz/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> result = (List<Category>) categoryRepository.findAll();

        if(result.isEmpty()){
            // NOT FOUND if no categories can be found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // OK
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // returns a list of quizzes using params
    @GetMapping("quizzes")
    public ResponseEntity<List<Quiz>> getQuizzes(@RequestParam(name = "results") final Optional<Integer> results
                        , @RequestParam(name = "sort") final Optional<Integer> sort
                        , @RequestParam(name = "category") final Optional<Integer> category_id) {

        Optional<List<Quiz>> result;

        // get quizzes
        if(sort.isPresent()){
            switch(sort.get()) {
                case 0:
                    // new
                    result = quizRepository.findAllByOrderByDateDesc();
                    break;
                case 1:
                    // old
                    result = quizRepository.findAllByOrderByDateAsc();
                    break;
                case 2:
                    // popular
                    result = quizRepository.findAllByOrderByViewsDesc();
                    break;
                case 3:
                    // unpopular
                    result = quizRepository.findAllByOrderByViewsAsc();         
                    break;
                default:
                    // if wrong sort given use new
                    result = quizRepository.findAllByOrderByDateDesc();
                    break;
            }
        } else {
            // if no sort given use new
            result = quizRepository.findAllByOrderByDateDesc();
        }

        if(!result.isPresent()){
            // NOT FOUND if no quizzes found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Quiz> quizzes = result.get();

        // limit by category
        if(category_id.isPresent()){
            // check if category exists

            Optional<Category> categoryResult = categoryRepository.findById(category_id.get());
            if(categoryResult.isPresent()){

                List<Quiz> removeList = new ArrayList<>();

                for (Quiz quiz : quizzes){
                    if(quiz.getCategory_id() != category_id.get()){
                        removeList.add(quiz);
                    }
                }

                for (Quiz quiz : removeList){
                    quizzes.remove(quiz);
                }

            } else {
                // BAD REQUEST if category id does not exist
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        // limit by results amount:

        // OK
        return ResponseEntity.status(HttpStatus.OK).body(quizzes);
    }

    // create a new quiz
    @PostMapping("/quiz")
    public ResponseEntity<Quiz> createQuiz(@RequestParam(name = "creator") final int creator_id
                        , @RequestParam(name = "category") final int category_id
                        , @RequestParam(name = "title") final String title
                        , @RequestParam(name = "description") final String description
                        , @RequestParam(name = "image") final Optional<Integer> image_id) {

        // check if given category and creator exist
        if(userRepository.existsById(creator_id) && categoryRepository.existsById(category_id)){

            Quiz newQuiz;

            // check if a image_id has been given
            if(image_id.isPresent()){
                newQuiz = new Quiz(creator_id, category_id, title, description, image_id.get());
            } else {
                newQuiz = new Quiz(creator_id, category_id, title, description);
            }
            
            try {
                quizRepository.save(newQuiz);
            } catch (DataIntegrityViolationException e) {
                // CONLFICT if quiz with title already exists
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }

            // CREATED
            return ResponseEntity.status(HttpStatus.CREATED).body(newQuiz);
        }

        // BAD REQUEST if creator or category doesnt exist
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    // create a new question
    // for now just text questions
    @PostMapping("/quiz/question")
    public ResponseEntity<QuestionText> createQuestion(@RequestParam(name = "quiz") final int quiz_id
                                    , @RequestParam(name = "question") final String question
                                    , @RequestParam(name = "answer") final String answer
                                    , @RequestParam(name = "position") final int position
                                    , @RequestParam(name = "image") final Optional<Integer> image_id) {
        
        if(quizRepository.existsById(quiz_id)) {

            QuestionText newQuestion;

            if(image_id.isPresent()){
                newQuestion = new QuestionText(quiz_id, question, answer, position, image_id.get());
            } else {
                newQuestion = new QuestionText(quiz_id, question, answer, position);
            }
            
            questionTextRepository.save(newQuestion);

            // CREATED
            return ResponseEntity.status(HttpStatus.CREATED).body(newQuestion);
        }

        // BAD REQUEST if quiz with quiz_id doesnt exist
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}