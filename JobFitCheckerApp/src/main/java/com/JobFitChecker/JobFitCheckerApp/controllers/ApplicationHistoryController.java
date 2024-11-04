package com.JobFitChecker.JobFitCheckerApp.controllers;

import com.JobFitChecker.JobFitCheckerApp.model.entities.ApplicationRecord;
import com.JobFitChecker.JobFitCheckerApp.model.data.ApplicationRecordDTO;
import com.JobFitChecker.JobFitCheckerApp.model.data.WeeklyApplicationCountDTO;
import com.JobFitChecker.JobFitCheckerApp.services.userActivity.ApplicationHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationHistoryController {
    private static final Logger log = LoggerFactory.getLogger(ApplicationHistoryController.class);

    private final ApplicationHistoryService applicationHistoryService;

    @Autowired
    public ApplicationHistoryController(ApplicationHistoryService applicationHistoryService) {
        this.applicationHistoryService = applicationHistoryService;
    }

    @PostMapping("/application")
    public ResponseEntity<String> handleAddApplicationRecord(@RequestBody ApplicationRecord record, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

//        if (session != null) {
//            User loggedInUser = (User) session.getAttribute("loggedInUser");
//            if (loggedInUser != null) {
//                Long loggedInUserId = loggedInUser.getUserId();
//
//                if (loggedInUserId != null) {
//                    try {
//                        loggedInUserId = 7L; // ToDO: delete the hardcoded userId after connecting to FE
//                        applicationHistoryService.addRecordToUser(loggedInUserId, record);
//                        return ResponseEntity.status(HttpStatus.OK).body("Successfully added application record.");
//                    } catch (Exception ex) {
//                        log.error("Failed to add application record {} for user {}", record, loggedInUser);
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Add application record failed");
//                    }
//                }
//            }
//        }
        long loggedInUserId = 7L; // ToDO: delete the hardcoded userId and uncomment above after connecting to FE
        try {
            ApplicationRecordDTO existingRecord = applicationHistoryService.retrieveApplicationRecords(
                    loggedInUserId, record.getCompany(), record.getPosition(), record.getJobId()
            );
            if (existingRecord != null)
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("You already added this application record. Please not add duplicate records!");

            applicationHistoryService.addRecordToUser(loggedInUserId, record);
            return ResponseEntity.ok("Successfully added application record.");

        } catch (Exception ex) {
            log.error("Failed to add application record {} for user {}", record, loggedInUserId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Add application record failed");
        }
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
        // ToDo: uncomment when changing to dynamic logged in user
    }

    @GetMapping("application-counts")
    public ResponseEntity<?> handleRetrieveApplicationCountsForUserWithinTwoMonths(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        long loggedInUserId = 7L; // ToDO: Change to dynamic userId
        try {
            List<WeeklyApplicationCountDTO> weeklyCounts = applicationHistoryService.retrieveApplicationCountsForUserInTwoMonths(loggedInUserId);
            return ResponseEntity.ok(weeklyCounts);
        } catch (Exception ex) {
            log.error("Failed to retrieve job application counts for user {} with previous two months: ", loggedInUserId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Retrieve application counts failed");
        }
    }

    @GetMapping("application-records")
    public ResponseEntity<?> handleCheckApplicationHistory(
            @RequestParam String company,
            @RequestParam String position,
            @RequestParam String jobId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        long loggedInUserId = 7L; // ToDO: Change to dynamic userId
        try {
            ApplicationRecordDTO record = applicationHistoryService.retrieveApplicationRecords(loggedInUserId, company, position, jobId);
            if (record == null)
                return ResponseEntity.ok("No matching records found");
            else
                return ResponseEntity.ok(record);
        } catch (Exception ex) {
            log.error("Failed to retrieve application records for user {} ", loggedInUserId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Retrieve application records failed");
        }
    }
}
