package com.quizmaker.backend.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Table used to keep track of completed quizzes per user.
@Entity
@Table(name="user_completed_quiz")
public class UserCompletedQuiz {

    // ids
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private int user_id;
    private int quiz_id;

    // statistics
    private int score;

    protected UserCompletedQuiz() {}

    public UserCompletedQuiz(int user_id, int quiz_id, int score) {
        this.user_id = user_id;
        this.quiz_id = quiz_id;
        this.score = score;
    }

    /** 
     * Getters and setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}