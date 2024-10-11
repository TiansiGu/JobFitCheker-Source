package com.JobFitChecker.JobFitCheckerApp.services.resumePostProcessWorkflow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class UpdateQualificationDataTest {

    @Autowired
    private UpdateQualificationActivity updateQualificationData;

    @Test
    public void testUpdateQualification() {
        // Create a Qualification object with test data
        Set<String> previousJobTitles = new HashSet<>();
        previousJobTitles.add("Software Engineer");
        previousJobTitles.add("HR Specialist");

        Set<String> skills = new HashSet<>();
        skills.add("Java");
        skills.add("Kotlin");
        skills.add("SQL");

        Qualification qualification = new Qualification(7L, "Master of Science", "Computer Science",
                "May 2026", previousJobTitles, skills);

        // Call the updateQualification method
        updateQualificationData.updateQualification(qualification);

    }
}
