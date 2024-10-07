package com.JobFitChecker.JobFitCheckerApp.Model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Username")
    private String username;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "ResumeKey")
    private String resumeKey;

    @Column(name = "Degree")
    private String degree;

    @Column(name = "Major")
    private String major;

    @Column(name = "GraduationDate")
    private String graduationDate;

    @Column(name = "PreviousJobTitles")
    private String previousJobTitles;

    @Column(name = "Skills")
    private String skills;

    // Constructor without userId (since it's auto-generated)
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Default constructor (required by JPA)
    public User() {

    }

    public Long getUserId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
