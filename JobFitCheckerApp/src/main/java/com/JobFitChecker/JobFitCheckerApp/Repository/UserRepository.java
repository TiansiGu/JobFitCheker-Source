package com.JobFitChecker.JobFitCheckerApp.Repository;

import com.JobFitChecker.JobFitCheckerApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
