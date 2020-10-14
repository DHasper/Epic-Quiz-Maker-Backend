package com.quizmaker.backend.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// Table for possible answers for a multiple choice quiz.
// Also contains wether the answer is correct or not.
@Entity
@Table(name="mc_answer")
public class AnswerMc {
    
    // ids
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private int question_id;

    // answer info
    private String answer;
    private boolean correct;

    protected AnswerMc() {}

    public AnswerMc(int question_id, String answer, boolean correct) {
        this.question_id = question_id;
        this.answer = answer;
        this.correct = correct;
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

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}