package com.JobFitChecker.JobFitCheckerApp.services.userActivity;

public final class AIFeedBack {
    private final boolean shouldApply;
    private final String reason;

    public AIFeedBack(boolean shouldApply, String reason) {
        this.shouldApply = shouldApply;
        this.reason = reason;
    }

    public boolean isShouldApply() {
        return shouldApply;
    }

    public String getReason() {
        return reason;
    }
}
