package com.JobFitChecker.JobFitCheckerApp.controllers;

import com.JobFitChecker.JobFitCheckerApp.model.ApplicationRecord;
import com.JobFitChecker.JobFitCheckerApp.model.User;
import com.JobFitChecker.JobFitCheckerApp.services.userActivity.ApplicationHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        long loggedInUserId = 7L; // ToDO: delete the hardcoded userId after connecting to FE
        try {
            applicationHistoryService.addRecordToUser(loggedInUserId, record);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully added application record.");
        } catch (Exception ex) {
            log.error("Failed to add application record {} for user {}", record, loggedInUserId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Add application record failed");
        }
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
        // ToDo: uncomment when changing to dynamic logged in user
    }
}
