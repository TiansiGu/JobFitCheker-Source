package com.JobFitChecker.JobFitCheckerApp.repository;

import java.time.LocalDateTime;

public class WeeklyApplicationCountDTO implements WeeklyApplicationCount {
    private LocalDateTime weekStart;
    private long applicationCount;

    public WeeklyApplicationCountDTO(LocalDateTime weekStart, long applicationCount) {
        this.weekStart = weekStart;
        this.applicationCount = applicationCount;
    }

    public LocalDateTime getWeekStart() {
        return weekStart;
    }

    public long getApplicationCount() {
        return applicationCount;
    }
}
