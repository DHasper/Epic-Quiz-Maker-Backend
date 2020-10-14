package com.quizmaker.backend.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Table for possible text questions.
// Only holds one correct answer.
@Entity
@Table(name="text_question")
public class QuestionText {

    // ids
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="quiz_id")
    private int quizId;

    // question data
    private int image_id;
    private String question;
    private String answer;
    private int position;

    protected QuestionText() {}

    public QuestionText(int quizId, String question, String answer, int position, int image_id) {
        this.quizId = quizId;
        this.image_id = image_id;
        this.question = question;
        this.answer = answer;
        this.position = position;
    }

    public QuestionText(int quizId, String question, String answer, int position) {
        this.quizId = quizId;
        this.question = question;
        this.answer = answer;
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

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getImageId() {
        return image_id;
    }

    public void setImageId(int image_id) {
        this.image_id = image_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}