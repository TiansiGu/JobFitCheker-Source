package com.JobFitChecker.JobFitCheckerApp.model.entities;

import jakarta.persistence.*;
import java.util.List;

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

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Degree")
    private String degree;

    @Column(name = "Major")
    private String major;

    @Column(name = "NeedSponsor")
    private String needSponsor;

    @Column(name = "GraduationDate")
    private String graduationDate;

    @Column(name = "PreviousJobTitles")
    private String previousJobTitles;

    @Column(name = "Skills", columnDefinition = "TEXT")
    private String skills;

    @OneToMany(mappedBy = "user")
    private List<ApplicationRecord> applicationRecords;


    // Constructor without userId (since it's auto-generated) // might not use this anymore
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Default constructor (required by JPA)
    protected User() {

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getNeedSponsor() {
        return needSponsor;
    }

    public void setNeedSponsor(String needSponsor) {
        this.needSponsor = needSponsor;
    }

    public String getResumeKey() {
        return resumeKey;
    }

    public String getMajor() {
        return major;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public String getPreviousJobTitles() {
        return previousJobTitles;
    }

    public String getSkills() {
        return skills;
    }

    @Override
    // Added for debugging, might want to delete in the end
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", resumeKey='" + resumeKey + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", degree='" + degree + '\'' +
                ", major='" + major + '\'' +
                ", needSponsor='" + needSponsor + '\'' +
                ", graduationDate='" + graduationDate + '\'' +
                ", previousJobTitles='" + previousJobTitles + '\'' +
                ", skills='" + skills + '\'' +
                '}';
    }
}
