package com.JobFitChecker.JobFitCheckerApp.model.data;

import java.time.LocalDateTime;

public interface ApplicationRecordDTO {
    String getCompany();
    String getPosition();
    Long getUserId();
    String getJobId();
    LocalDateTime getCreateTime();
}
