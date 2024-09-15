package com.JobFitChecker.JobFitCheckerApp.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ResumeController {

    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    @PostMapping("/upload-resume")
    public String handleResumeUpload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        if (file.isEmpty())
            redirectAttributes.addFlashAttribute("error", "Please upload a resume first");
        if (!file.getContentType().equals("application/pdf"))
            redirectAttributes.addFlashAttribute("error", "Please only upload PDF resumes");

        try {
            // logic of putRequest
            redirectAttributes.addFlashAttribute("message",
                    "Successfully uploaded " + file.getOriginalFilename());
        } catch (Exception e) {
            logger.error("Failed to upload file {}: {}, {}", file.getOriginalFilename(),
                    e.getMessage(), e.getStackTrace());
            redirectAttributes.addFlashAttribute("error",
                    "Upload failed: ");
        }

        return "redirect:/";
    }
}