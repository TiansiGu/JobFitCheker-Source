package com.JobFitChecker.JobFitCheckerApp.model.data;

import java.time.LocalDateTime;

public final class WeeklyApplicationCountDTOClass implements WeeklyApplicationCountDTO {
    private LocalDateTime weekStart;
    private long applicationCount;

    public WeeklyApplicationCountDTOClass(LocalDateTime weekStart, long applicationCount) {
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
