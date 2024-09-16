package com.JobFitChecker.JobFitCheckerApp.Controller;

import com.JobFitChecker.JobFitCheckerApp.Services.UserActivity.ResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.CompletableFuture;

@Controller
public class ResumeController {

    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);

    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload-resume")
    public CompletableFuture<String> handleResumeUpload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        if (file.isEmpty())
            redirectAttributes.addFlashAttribute("error", "Please upload a resume first");

        if (!file.getContentType().equals("application/pdf"))
            redirectAttributes.addFlashAttribute("error", "Please only upload PDF resumes");

        return CompletableFuture.supplyAsync(() -> {
            try {
                resumeService.putResume(file);
                redirectAttributes.addFlashAttribute("message",
                        "Successfully uploaded " + file.getOriginalFilename());
            } catch (Exception ex) {
                log.error("Failed to upload file {}: {}, {}",
                        file.getOriginalFilename(), ex.getMessage(), ex.getStackTrace());
                redirectAttributes.addFlashAttribute("error",
                        "Upload failed");
            }
            return "redirect:/";
        });
    }
}