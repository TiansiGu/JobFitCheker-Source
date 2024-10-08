package com.JobFitChecker.JobFitCheckerApp.Controller;

import com.JobFitChecker.JobFitCheckerApp.Services.UserActivity.ResumeService;
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
    public ResponseEntity<String> handleUploadResume(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a resume first");

        if (!file.getContentType().equals("application/pdf"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please only upload PDF resumes");

        try {
            long userId = 7L; //ToDo: fetch real userId from session
            resumeService.putResume(userId, file);
            return ResponseEntity.status(HttpStatus.OK).body("Upload successful");
        } catch (Exception ex) {
            log.error("Failed to upload file {}: ", file.getOriginalFilename() + ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @DeleteMapping("/delete-resume")
    public ResponseEntity<String> handleDeleteResume() {
        String resumeKey = "7@Resume_Tiansi Gu.pdf"; // ToDo: change to get resumeKey from session or calling getResumeKey

        try {
            long userId = 7L; //ToDo: fetch real userId from session
            resumeService.deleteResume(userId, resumeKey);
            return ResponseEntity.status(HttpStatus.OK).body("Delete successful");
        } catch (Exception ex) {
            log.error("Failed to delete file {}: ", resumeKey + ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }
}