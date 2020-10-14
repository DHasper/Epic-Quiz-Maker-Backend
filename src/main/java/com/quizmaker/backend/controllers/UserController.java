package com.quizmaker.backend.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import com.quizmaker.backend.models.User;
import com.quizmaker.backend.repositories.UserRepository;
import com.quizmaker.backend.utils.JwtGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
public class UserController {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    // authorize a user
    @PostMapping("/auth")
    public ResponseEntity<Map<String, Boolean>> register(@RequestParam(name = "token") final String token) {

        boolean tokenValid = new JwtGenerator().checkToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(
            Collections.singletonMap("valid", tokenValid)
        );
    }
    
    // register a new user
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestParam(name = "username") final String username
                        , @RequestParam(name = "password") final String password) {

        // creates a new user - password gets hashed
        User newUser = new User(username, encoder.encode(password));
        try {
            userRepository.save(newUser); 
        } catch (DataIntegrityViolationException e) {
            // CONFLICT if user with username already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        
        // CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // login a user
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam(name = "username") final String username
                        , @RequestParam(name = "password") final String password) {
        
        if (username != null && password != null) {
            // get users password and ID
            String userPassword;
            int userID;

            // check if user exists
            if (userRepository.existsByUsername(username)) {
                User user = userRepository.findByUsername(username).iterator().next();
                userPassword = user.getPassword();
                userID = user.getId();
            } else {
                // NOT FOUND if no user with username exists
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // check if password matches
            if (encoder.matches(password, userPassword)) {
                // generate jwt token
                String token = new JwtGenerator().getToken(username, userID);

                // OK
                return ResponseEntity.status(HttpStatus.OK).body(
                    Collections.singletonMap("token", token)
                );
            }
        }

        // UNAUTHORIZED if password not correct
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // get username
    @GetMapping("user/{uid}")
    public ResponseEntity<Map<String, String>> getUsername(@PathVariable final int uid) {

        // check if user exists
        if(userRepository.existsById(uid)){
            Optional<User> userResult = userRepository.findById(uid);
            if(userResult.isPresent()){
                // OK
                return ResponseEntity.status(HttpStatus.OK).body(
                    Collections.singletonMap("name", userResult.get().getUsername())
                );
            }  
        }

        // NOT FOUND if nu user exists with given uid
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}