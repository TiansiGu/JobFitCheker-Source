package com.JobFitChecker.JobFitCheckerApp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "application_records")
public class ApplicationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company")
    private String company;

    @Column(name = "position")
    private String position;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false)
    private User user;

    // Default constructor, for JPA
    protected ApplicationRecord() {

    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getJobId() {
        return jobId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public User getUser() {
        return user;
    }
}
