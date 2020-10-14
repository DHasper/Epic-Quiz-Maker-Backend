package com.quizmaker.backend.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.sql.DataSource;

import com.quizmaker.backend.models.Category;
import com.quizmaker.backend.models.QuestionText;
import com.quizmaker.backend.models.Quiz;
import com.quizmaker.backend.models.User;
import com.quizmaker.backend.repositories.CategoryRepository;
import com.quizmaker.backend.repositories.QuestionTextRepository;
import com.quizmaker.backend.repositories.QuizRepository;
import com.quizmaker.backend.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class was used to generate random data for testing purposes.
 */
@RestController
public class TestDataController {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final Random random = new Random();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionTextRepository questionTextRepository;

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {return dataSource.getConnection();}

    @GetMapping("/test")
    public String generateTestData() throws SQLException {

        // generate category
        categoryRepository.deleteAll();

        Category category1 = new Category("Sport");
        Category category2 = new Category("Music");
        Category category3 = new Category("Games");
        Category category4 = new Category("Food");
        Category category5 = new Category("Movies");
        Category category6 = new Category("Geography");
        Category category7 = new Category("Science");
        Category category8 = new Category("Language");
        Category category9 = new Category("History");
        Category category10 = new Category("Miscellaneous");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);
        categoryRepository.save(category6);
        categoryRepository.save(category7);
        categoryRepository.save(category8);
        categoryRepository.save(category9);
        categoryRepository.save(category10);
  
        // generate test users
        userRepository.deleteAll();
        final User testUser1 = new User("Dylan", encoder.encode("test"));
        final User testUser2 = new User("Dennis", encoder.encode("test"));
        final User testUser3 = new User("Omar", encoder.encode("test"));
        final User testUser4 = new User("Shaquille", encoder.encode("test"));
        final User testUser5 = new User("xX_Quizmaker420_Xx", encoder.encode("test"));
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        userRepository.save(testUser4);
        userRepository.save(testUser5);


        // generature users
        // for(int i=0; i<100; i++){

        //     final String username = generateString(7);
        //     final String password = encoder.encode("Wachtwoord1");

        //     try {
        //         final User user = new User(username, password);
        //         userRepository.save(user);
        //     } catch (final DataIntegrityViolationException e){ }
        // }

        // get available player ids
        ArrayList<Integer> player_ids = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(
            "SELECT id FROM user")) {
        
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    player_ids.add(rs.getInt("id"));
                }
            }
        }

        // get available category ids
        ArrayList<Integer> category_ids = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(
            "SELECT id FROM category")) {
        
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    category_ids.add(rs.getInt("id"));
                }
            }
        }

        quizRepository.deleteAll();
        questionTextRepository.deleteAll();

        // generate random quizzes
        for(int i=0; i<50; i++){
            int creator_id = player_ids.get(random.nextInt(player_ids.size()));
            int category_id = category_ids.get(random.nextInt(category_ids.size()));
            String title = generateString(8);
            String description = generateString(50);

            Quiz quiz = new Quiz(creator_id, category_id, title, description);
            quizRepository.save(quiz);

            int questionAmount = random.nextInt(10) + 1;
            for(int o=0; o<questionAmount; o++){
                String question_text = generateString(random.nextInt(10) + 1) + " " + generateString(random.nextInt(10) + 1) + 
                generateString(random.nextInt(10) + 1) +  generateString(random.nextInt(10) + 1) +"?";

                String answer = generateString(random.nextInt(10) + 1);

                QuestionText question = new QuestionText(quiz.getId(), question_text, answer, o);
                questionTextRepository.save(question);
            }
        }

        return "";
    }

    public String generateString(final int n) {
        final int leftLimit = 48; // numeral '0'
        final int rightLimit = 122; // letter 'z'

        return random.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(n)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
    }
}