package com.JobFitChecker.JobFitCheckerApp.repository;

import com.JobFitChecker.JobFitCheckerApp.model.data.ApplicationRecordDTO;
import com.JobFitChecker.JobFitCheckerApp.model.data.WeeklyApplicationCountDTO;
import com.JobFitChecker.JobFitCheckerApp.model.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET resume_key = :resumeKey WHERE id = :id", nativeQuery = true)
    void updateResumeKey(@Param("id") long id, @Param("resumeKey") String resumeKey);

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

    @Transactional
    @Query(value = "SELECT DATE_TRUNC('week', create_time) AS week_start," + "COUNT(*) AS application_count " +
            "FROM application_records WHERE user_id = :userId AND create_time >= :createTime " +
            "GROUP BY week_start ORDER BY week_start", nativeQuery = true)
    List<WeeklyApplicationCountDTO> retrieveApplicationCountsForUserWithinTime(
            @Param("userId") long userId,
            @Param("createTime") LocalDateTime createTime
    );

    @Transactional
    @Query(value = "SELECT * FROM application_records " +
            "WHERE user_id = :userId " +
            "AND company = :company " +
            "AND position LIKE :positionParam " +
            "AND (:jobId IS NULL OR job_id = :jobId)",
            nativeQuery = true)
    List<ApplicationRecordDTO> retrieveApplicationRecords(
            @Param("userId") long userId,
            @Param("company") String company,
            @Param("positionParam") String positionParam,
            @Param("jobId") String jobId
    );
}
