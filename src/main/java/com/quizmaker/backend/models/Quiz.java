package com.quizmaker.backend.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Table for quizzes.
// Holds information about the quiz, but also some statistics.
@Entity
@Table(name="quiz", uniqueConstraints={@UniqueConstraint(columnNames = {"title"})})
public class Quiz {

    // ids
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private int creator_id;
    private int category_id;

    // information
    @Column(nullable=false, unique=true)
    private String title;
    
    private String description;
    private int image;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // statistics
    private int views;
    private int completions;
    private int average_score;

    protected Quiz() {}

    public Quiz(int creator_id, int category_id, String title, String description, int image) {
        this.creator_id = creator_id;
        this.category_id = category_id;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public Quiz(int creator_id, int category_id, String title, String description) {
        this.creator_id = creator_id;
        this.category_id = category_id;
        this.title = title;
        this.description = description;
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

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getCompletions() {
        return completions;
    }

    public void setCompletions(int completions) {
        this.completions = completions;
    }

    public int getAverage_score() {
        return average_score;
    }

    public void setAverage_score(int average_score) {
        this.average_score = average_score;
    }
}