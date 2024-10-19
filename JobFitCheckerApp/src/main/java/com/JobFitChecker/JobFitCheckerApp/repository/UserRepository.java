package com.JobFitChecker.JobFitCheckerApp.repository;

import com.JobFitChecker.JobFitCheckerApp.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET resume_key = :resumeKey WHERE id = :id", nativeQuery = true)
    void updateResumeKey(@Param("id") long id, @Param("resumeKey") String resumeKey);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET degree = :degree, major = :major, graduation_date = :graduationDate, " +
                    "previous_job_titles = :previousJobTitles, skills = :skills WHERE id = :id", nativeQuery = true)
    void updateQualification(
            @Param("id") long id,
            @Param("degree") String degree,
            @Param("major") String major,
            @Param("graduationDate") String graduationDate,
            @Param("previousJobTitles") String previousJobTitles,
            @Param("skills") String skills
    );

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO application_records (company, position, job_id, create_time, user_id)" +
            "VALUES (:company, :position, :jobId, :createTime, :userId)", nativeQuery = true)
    void addApplicationRecordToUser(
            @Param("company") String company,
            @Param("position") String position,
            @Param("jobId") String jobId,
            @Param("createTime") LocalDateTime createTime,
            @Param("userId") long userId
    );
}
