package com.JobFitChecker.JobFitCheckerApp.model.data;

import java.time.LocalDateTime;

public interface WeeklyApplicationCountDTO {
    LocalDateTime getWeekStart();
    long getApplicationCount();
}
