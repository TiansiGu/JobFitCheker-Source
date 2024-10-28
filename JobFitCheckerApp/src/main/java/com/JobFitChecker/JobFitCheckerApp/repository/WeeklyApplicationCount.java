package com.JobFitChecker.JobFitCheckerApp.repository;

import java.time.LocalDateTime;

public interface WeeklyApplicationCount {
    LocalDateTime getWeekStart();
    long getApplicationCount();
}
