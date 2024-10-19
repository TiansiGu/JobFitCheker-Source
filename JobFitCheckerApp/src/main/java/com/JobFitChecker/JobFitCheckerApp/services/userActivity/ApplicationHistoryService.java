package com.JobFitChecker.JobFitCheckerApp.services.userActivity;

import com.JobFitChecker.JobFitCheckerApp.model.ApplicationRecord;
import com.JobFitChecker.JobFitCheckerApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public final class ApplicationHistoryService {
    private static final Logger log = LoggerFactory.getLogger(ApplicationHistoryService.class);

    private final UserRepository userRepository;

    @Autowired
    public ApplicationHistoryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addRecordToUser(long userId, ApplicationRecord record) {
        LocalDateTime createTime = LocalDateTime.now();

        log.info("Adding job application record for user {} at {}", userId, createTime);

        userRepository.addApplicationRecordToUser(
                record.getCompany(),
                record.getPosition(),
                record.getJobId(),
                createTime,
                userId
        );

        log.info("Successfully added job application record for user {} at {}", userId, createTime);
    }
}
