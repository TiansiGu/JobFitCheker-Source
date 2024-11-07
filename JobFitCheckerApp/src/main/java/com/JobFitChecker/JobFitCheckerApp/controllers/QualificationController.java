package com.JobFitChecker.JobFitCheckerApp.controllers;

import com.JobFitChecker.JobFitCheckerApp.model.data.AIFeedBack;
import com.JobFitChecker.JobFitCheckerApp.services.userActivity.QualificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.JSqlParserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.JobFitChecker.JobFitCheckerApp.model.entities.User;
import com.google.gson.*;

@RestController
public class QualificationController {
    private static final Logger log = LoggerFactory.getLogger(QualificationController.class);

    private final QualificationService qualificationService;

    @Autowired
    public QualificationController(QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @PostMapping(value = "check-qualification")
    public ResponseEntity<?> handleCheckQualification(@RequestBody String jobDescriptionJsonObj, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        JsonObject jsonObject = JsonParser.parseString(jobDescriptionJsonObj).getAsJsonObject();
        String jobDescription = jsonObject.get("jobDescription").getAsString();

        if (jobDescription == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing 'jobDescription' field");
        }

        if (session == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");

        Long loggedInUserId = ((User) session.getAttribute("loggedInUser")).getUserId();

        try {
            AIFeedBack feedBack = qualificationService.askAICheckQualifications(loggedInUserId, jobDescription);
            System.out.println("feedback: " + feedBack.isShouldApply() + feedBack.getReason());
            return ResponseEntity.ok(feedBack);
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
            log.error("Failed to check qualification for user {}: ", loggedInUserId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Qualification check failed");
        }
    }
}
