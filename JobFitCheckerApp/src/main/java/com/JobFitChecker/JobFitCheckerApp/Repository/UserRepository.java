package com.JobFitChecker.JobFitCheckerApp.Repository;

import com.JobFitChecker.JobFitCheckerApp.Model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
public interface UserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET resume_key = :resumeKey WHERE id = :id", nativeQuery = true)
    public int updateResumeKey(@Param("id") long id, @Param("resumeKey") String resumeKey);

}
