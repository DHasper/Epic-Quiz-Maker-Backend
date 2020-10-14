package com.quizmaker.backend.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Table for multiple choice questions.
@Entity
@Table(name="mc_question")
public class QuestionMc {

    // ids
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private int quiz_id;

    // question data
    private String image;
    private String question;
    private int position;

    protected QuestionMc() {}

    public QuestionMc(int quiz_id, String image, String question, int position) {
        this.quiz_id = quiz_id;
        this.image = image;
        this.question = question;
        this.position = position;
    }

    public QuestionMc(int quiz_id, String question, int position) {
        this.quiz_id = quiz_id;
        this.question = question;
        this.position = position;
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

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getposition() {
        return position;
    }

    public void setposition(int position) {
        this.position = position;
    }
}