package com.JobFitChecker.JobFitCheckerApp.controllers;

import com.JobFitChecker.JobFitCheckerApp.model.data.AIFeedBack;
import com.JobFitChecker.JobFitCheckerApp.services.userActivity.QualificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QualificationController {
    private static final Logger log = LoggerFactory.getLogger(QualificationController.class);

    private final QualificationService qualificationService;

    @Autowired
    public QualificationController(QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @GetMapping(value = "check-qualification")
    public ResponseEntity<?> handleCheckQualification(@RequestParam("jd") String jobDescription, HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session == null)
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
//
//        Long loggedInUserId = ((User) session.getAttribute("loggedInUser")).getUserId();
        Long loggedInUserId = 11L; //ToDo: change to dynamic user using above code after frontend is added

        try {
            AIFeedBack feedBack = qualificationService.askAICheckQualifications(loggedInUserId, jobDescription);
            return ResponseEntity.ok(feedBack);
        } catch (Exception ex) {
            log.error("Failed to check qualification for user {}: ", loggedInUserId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Qualification check failed");
        }
    }
}
