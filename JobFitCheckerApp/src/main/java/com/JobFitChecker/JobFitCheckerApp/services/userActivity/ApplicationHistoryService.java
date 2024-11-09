package com.JobFitChecker.JobFitCheckerApp.services.userActivity;

import com.JobFitChecker.JobFitCheckerApp.model.entities.ApplicationRecord;
import com.JobFitChecker.JobFitCheckerApp.model.data.ApplicationRecordDTO;
import com.JobFitChecker.JobFitCheckerApp.repository.UserRepository;
import com.JobFitChecker.JobFitCheckerApp.model.data.WeeklyApplicationCountDTO;
import com.JobFitChecker.JobFitCheckerApp.model.data.WeeklyApplicationCountDTOClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

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

    public List<WeeklyApplicationCountDTO> retrieveApplicationCountsForUserInTwoMonths(long userId) {
        LocalDate currentDate = LocalDate.now();
        LocalDate dateTwoMonthsAgo = currentDate.minusMonths(2);
        LocalDate firstMonday = dateTwoMonthsAgo.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime firstMondayAtStartOfDay = firstMonday.atStartOfDay();

        log.info("Retrieving job application records for user {} since {}", userId, firstMondayAtStartOfDay);

        List<WeeklyApplicationCountDTO> counts = userRepository.retrieveApplicationCountsForUserWithinTime(userId, firstMondayAtStartOfDay);

        log.info("Successfully retrieved job application records for user {} since {}: {}", userId, firstMondayAtStartOfDay, counts);

        List<LocalDate> mondays = new ArrayList<>();
        LocalDate date = firstMonday;
        while (!date.isAfter(currentDate)) {
            mondays.add(date);
            date = date.plusDays(7);
        }

        List<WeeklyApplicationCountDTO> completeCounts = new ArrayList<>();
        int k = 0;
        for (LocalDate monday : mondays) {
            if (k < counts.size() && monday.isEqual(counts.get(k).getWeekStart().toLocalDate())) {
                completeCounts.add(counts.get(k));
                k++;
            } else {
                completeCounts.add(new WeeklyApplicationCountDTOClass(monday.atStartOfDay(), 0));
            }
        }

        return completeCounts;
    }

    public ApplicationRecordDTO retrieveApplicationRecords(long userId, String company, String position, String jobId) {
        if (jobId != null && (jobId.trim().isEmpty() || jobId.equals("undefined")))
            jobId = null;

        log.info("Retrieving application record for user {} at company: {}, position: {}, jobId: {}...", userId, company, position, jobId);

        String positionParam = "%" + position + "%";

        List<ApplicationRecordDTO> records = userRepository.retrieveApplicationRecords(userId, company, positionParam, jobId);

        log.info("Successfully Retrieved application record for user {} matching company: {}, position: {}, jobId: {}: {}",
                userId, company, position, jobId, records);

        return records.isEmpty()? null : records.get(0);
    }
}
