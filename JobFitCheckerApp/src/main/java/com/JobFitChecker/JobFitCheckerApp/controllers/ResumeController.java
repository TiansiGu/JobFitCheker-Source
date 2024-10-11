package com.JobFitChecker.JobFitCheckerApp.controllers;

import com.JobFitChecker.JobFitCheckerApp.model.User;
import com.JobFitChecker.JobFitCheckerApp.services.userActivity.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

@RestController
public class ResumeController {

    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);

    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload-resume")
    public ResponseEntity<String> handleUploadResume(@RequestParam("resume") MultipartFile file, HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false to prevent creating a new session if none exists
        if (session == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");

        if (file.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a resume first");

        if (!file.getContentType().equals("application/pdf"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please only upload PDF resumes");

        try {
            Long loggedInUserId = ((User) session.getAttribute("loggedInUser")).getUserId();
            resumeService.putResume(loggedInUserId, file);
            return ResponseEntity.status(HttpStatus.OK).body("Upload successful");
        } catch (Exception ex) {
            log.error("Failed to upload file {}: ", file.getOriginalFilename() + ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @DeleteMapping("/delete-resume")
    public ResponseEntity<String> handleDeleteResume(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false to prevent creating a new session if none exists
        if (session == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
        Long loggedInUserId = ((User) session.getAttribute("loggedInUser")).getUserId();

        try {
            resumeService.deleteResume(loggedInUserId);
            return ResponseEntity.status(HttpStatus.OK).body("Delete successful");
        } catch (Exception ex) {
            log.error("Failed to delete resume for user {}: ", loggedInUserId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }
}