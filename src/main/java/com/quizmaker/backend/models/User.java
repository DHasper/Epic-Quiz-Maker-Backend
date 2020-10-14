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

// Table that holds user information such as usernames and passwords.
@Entity
@Table(name="user", uniqueConstraints={@UniqueConstraint(columnNames = {"username"})})
public class User {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    // user info
    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date register_date;

    protected User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegister_date() {
        return register_date;
    }

    public void setRegister_date(Date register_date) {
        this.register_date = register_date;
    }
}